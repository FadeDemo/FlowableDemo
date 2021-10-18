package org.fade.demo.flowabledemo.bpmn.constructs.gateway;

import cn.hutool.core.lang.Assert;
import org.fade.demo.flowabledemo.bpmn.util.DBUtil;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flowable.engine.impl.test.TestHelper.assertProcessEnded;

/**
 * @author fade
 * @date 2021/10/13
 */
public class ErrorBoundaryEvent {

    private static final Logger logger = LoggerFactory.getLogger(ErrorBoundaryEvent.class);

    public static void main(String[] args) {
        logger.info("开始执行流程");
        // 设置认证用户 kermit被保存至initiator变量
        Authentication.setAuthenticatedUserId("kermit");
        // 获取引擎配置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(DBUtil.getJavaJdbcUrl())
                .setJdbcUsername(DBUtil.getUsername())
                .setJdbcPassword(DBUtil.getPassword())
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 获取引擎
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("constructs/gateway/reviewSalesLead.bpmn20.xml")
                .deploy();
        // 启动流程实例
        Map<String, Object> variables = new HashMap<>(16);
        variables.put("details", "very interesting");
        variables.put("customerName", "Alfresco");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String procId = runtimeService.startProcessInstanceByKey("reviewSaledLead", variables).getId();
        // 处理第一个用户任务
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskAssignee("kermit").processInstanceId(procId).singleResult();
        Assert.isTrue(task.getName().equals("Provide new sales lead"));
        taskService.complete(task.getId());
        // 进入子流程
        // 查询会计组用户任务
        Task ratingTask = taskService.createTaskQuery().taskCandidateGroup("accountancy").processInstanceId(procId).singleResult();
        Assert.isTrue(ratingTask.getName().equals("Review customer rating"));
        // 处理经理组用户任务
        Task profitabilityTask = taskService.createTaskQuery().taskCandidateGroup("management").processInstanceId(procId).singleResult();
        Assert.isTrue(profitabilityTask.getName().equals("Review profitability"));
        variables = new HashMap<>(16);
        // 设置为信息不足够，走向错误结束事件
        variables.put("notEnoughInformation", true);
        taskService.complete(profitabilityTask.getId(), variables);
        // 被错误边界事件捕获后走向提供额外信息的用户任务
        Task provideDetailsTask = taskService.createTaskQuery().taskAssignee("kermit").processInstanceId(procId).singleResult();
        Assert.isTrue(provideDetailsTask.getName().equals("Provide additional details"));
        taskService.complete(provideDetailsTask.getId());
        // 重新进入子流程
        List<Task> reviewTasks = taskService.createTaskQuery().orderByTaskName().asc().processInstanceId(procId).list();
        assertThat(reviewTasks)
                .extracting(Task::getName)
                .containsExactly("Review customer rating", "Review profitability");
        // 完成会计组和经理组的用户任务
        taskService.complete(reviewTasks.get(0).getId());
        variables.put("notEnoughInformation", false);
        taskService.complete(reviewTasks.get(1).getId(), variables);
        // 判断流程是否结束
        assertProcessEnded(processEngine, procId);
        // 销毁认证用户
        Authentication.setAuthenticatedUserId(null);
        logger.info("执行流程完毕");
    }

}
