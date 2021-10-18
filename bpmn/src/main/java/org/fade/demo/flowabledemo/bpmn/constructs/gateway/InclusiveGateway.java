package org.fade.demo.flowabledemo.bpmn.constructs.gateway;

import org.fade.demo.flowabledemo.bpmn.util.DBUtil;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fade
 * @date 2021/10/15
 */
public class InclusiveGateway {

    private static final Logger logger = LoggerFactory.getLogger(InclusiveGateway.class);

    public static void main(String[] args) {
        // 获取引擎配置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(DBUtil.getMemoryJdbcUrl())
                .setJdbcUsername(DBUtil.getUsername())
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 获取引擎
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("constructs/gateway/inclusiveGateway.bpmn20.xml")
                .deploy();
        // 设置流程变量并启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HashMap<String, Object> processVariables = new HashMap<>(16);
        processVariables.put("paymentReceived", false);
//        processVariables.put("paymentReceived", true);
//        processVariables.put("shipOrder", false);
        processVariables.put("shipOrder", true);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("inclusiveGateway", processVariables);
        // 查询用户任务数量
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(pi.getProcessInstanceId())
                .orderByTaskName()
                .asc()
                .list();
        logger.info("The number of task before join gateway is " + tasks.size());
        tasks.forEach(task -> {
            logger.info("Task's name is " + task.getName());
            taskService.complete(task.getId());
        });
        tasks = taskService.createTaskQuery()
                .processInstanceId(pi.getProcessInstanceId())
                .orderByTaskName()
                .asc()
                .list();
        assertThat(tasks.size()).isEqualTo(1);
    }

}
