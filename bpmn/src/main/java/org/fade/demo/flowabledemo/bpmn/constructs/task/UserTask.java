package org.fade.demo.flowabledemo.bpmn.constructs.task;

import cn.hutool.core.lang.Assert;
import org.fade.demo.flowabledemo.bpmn.util.DBUtil;
import org.flowable.engine.*;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fade
 * @date 2021/10/18
 */
public class UserTask {

    private static final String TASK_NAME = "Important task";

    private static final Logger logger = LoggerFactory.getLogger(UserTask.class);

    public static void main(String[] args) {
        // 获取流程引擎配置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(DBUtil.getMemoryJdbcUrl())
                .setJdbcUsername(DBUtil.getUsername())
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 获取流程引擎
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("constructs/task/userTask.bpmn20.xml")
                .deploy();
        // 启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>(16);
        // java.util.Date格式的到期时间
//        Date dueDate = new Date();
//        dueDate.setTime(dueDate.getTime() + 20*60*1000L);
//        variables.put("dueDate", dueDate);
        // 通过流程变量指定flowable:assignee
//        variables.put("assignee", "kermit");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("userTask", variables);
//        ProcessInstance pi = runtimeService.startProcessInstanceByKey("userTask");
        // 查询用户任务
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi.getProcessInstanceId())
                .taskName(TASK_NAME)
                .singleResult();

        // 验证获取任务描述
//        logger.info(task.getDescription());

        // 验证设置到期时间
//        logger.info("The due date of the task is " + task.getDueDate());
        // 通过TaskService设置或修改到期时间
//        dueDate.setTime(dueDate.getTime() + 40*60*1000L);
//        taskService.setDueDate(task.getId(), dueDate);
//        Task dueDateTask = taskService.createTaskQuery().taskDueDate(dueDate).singleResult();
//        Assert.isTrue(task.getId().equals(dueDateTask.getId()), "不能修改任务id");
//        logger.info("The due date of the task is " + dueDateTask.getDueDate());

        // 验证任务分配
        // 直接分配给一个用户
//        String assignee = task.getAssignee();
//        logger.info("Task's assignee is " + assignee);
//        Task assigneeTask = taskService.createTaskQuery().taskAssignee("kermit").singleResult();
//        Assert.isTrue(task.getId().equals(assigneeTask.getId()), "不能修改任务id");
        // 作为候选任务分配给用户或组
//        Task candidateUserTask = taskService.createTaskQuery().taskCandidateUser("kermit").singleResult();
//        Assert.isTrue(task.getId().equals(candidateUserTask.getId()), "不能修改任务id");
//        Task candidateGroupTask = taskService.createTaskQuery().taskCandidateGroup("management").singleResult();
//        Assert.isTrue(task.getId().equals(candidateGroupTask.getId()));
        // 验证默认是组
//        candidateUserTask = taskService.createTaskQuery().taskCandidateUser("management").singleResult();
//        Assert.notNull(candidateUserTask, "默认是组");

        
    }

}
