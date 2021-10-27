package org.fade.demo.flowabledemo.bpmn.constructs.task.usertask;

import org.assertj.core.api.Assertions;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fade
 * @date 2021/10/24
 */
public class UserTaskInSpringEnv {

    private static final String TASK_NAME = "Important task";

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("constructs/task/flowable.cfg.xml");
        // 部署流程定义
        RepositoryService repositoryService = context.getBean("repositoryService", RepositoryService.class);
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("constructs/task/userTask.bpmn20.xml")
                .deploy();
        // 启动流程实例
        RuntimeService runtimeService = context.getBean("runtimeService", RuntimeService.class);
        Map<String, Object> variables = new HashMap<>(16);
        variables.put("emp", "kermit");
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("userTask", variables);
        // 查询用户任务
        TaskService taskService = context.getBean("taskService", TaskService.class);
        Task task = taskService.createTaskQuery()
                .processInstanceId(pi.getProcessInstanceId())
                .taskName(TASK_NAME)
                .singleResult();
        // 直接分配给一个用户
//        Assertions.assertThat(task.getAssignee()).isEqualTo("kermit");
        // 作为候选任务分配
        Task candidateTask = taskService.createTaskQuery().taskCandidateUser("kermit").singleResult();
        Assertions.assertThat(task.getId()).isEqualTo(candidateTask.getId());
    }

}
