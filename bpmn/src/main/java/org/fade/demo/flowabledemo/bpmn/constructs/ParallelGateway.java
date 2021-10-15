package org.fade.demo.flowabledemo.bpmn.constructs;

import org.fade.demo.flowabledemo.bpmn.util.DBUtil;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author fade
 * @date 2021/10/15
 */
public class ParallelGateway {

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
                .addClasspathResource("constructs/parallelGatewayTest.bpmn20.xml")
                .deploy();
        // 启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("parallelGatewayTest");
        TaskService taskService = processEngine.getTaskService();
        TaskQuery query = taskService.createTaskQuery()
                .processInstanceId(pi.getId())
                .orderByTaskName()
                .asc();
        // 查询此时的任务是不是两个
        List<Task> tasks = query.list();
        assertThat(tasks.size()).isEqualTo(2);
        // 判断第一个任务是不是"Receive Payment"
        Task task1 = tasks.get(0);
        assertThat(task1.getName()).isEqualTo("Receive Payment");
        // 判断第二个任务是不是“Ship Order”
        Task task2 = tasks.get(1);
        assertThat(task2.getName()).isEqualTo("Ship Order");
        // 完成上面的两个用户任务
        taskService.complete(task1.getId());
        taskService.complete(task2.getId());
        List<Task> list = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).list();
        // 判断此时是否只有一个用户任务
        assertThat(list.size()).isEqualTo(1);
        // 判断当前用户任务是不是"Archive Order"
        assertThat(list.get(0).getName()).isEqualTo("Archive Order");
    }

}
