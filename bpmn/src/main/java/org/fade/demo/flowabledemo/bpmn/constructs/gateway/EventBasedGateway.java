package org.fade.demo.flowabledemo.bpmn.constructs.gateway;

import org.fade.demo.flowabledemo.bpmn.util.DBUtil;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.flowable.engine.impl.test.TestHelper.assertProcessEnded;

/**
 * @author fade
 * @date 2021/10/15
 */
public class EventBasedGateway {

    private static final Logger logger = LoggerFactory.getLogger(EventBasedGateway.class);

    public static void main(String[] args) {
        // 获取引擎配置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(DBUtil.getMemoryJdbcUrl())
                .setJdbcUsername(DBUtil.getUsername())
                .setJdbcDriver("org.h2.Driver")
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                // 需要开启异步执行器
                .setAsyncExecutorActivate(true);
        // 获取引擎
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("constructs/gateway/eventBasedGateway.bpmn20.xml")
                .deploy();
        // 启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("catchSignal");
        // 等2分钟继续执行
        try {
            Thread.sleep(45000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertProcessEnded(processEngine, pi.getProcessInstanceId());
        logger.info("Process is finished");
        // 或者发送信号继续执行
//        runtimeService.signalEventReceived("alert");
//        TaskService taskService = processEngine.getTaskService();
//        List<Task> tasks = taskService.createTaskQuery()
//                .processInstanceId(pi.getProcessInstanceId())
//                .orderByTaskName()
//                .asc()
//                .list();
//        logger.info("There is(are) " + tasks.size() + " task(s) currently");
//        tasks.forEach(task -> {
//            logger.info("Task's name is " + task.getName());
//            taskService.complete(task.getId());
//        });
//        assertProcessEnded(processEngine, pi.getProcessInstanceId());
        // TODO: 验证流程引擎即使结束也未修改ProcessInstance的状态
//        logger.info("Process is finished: " + pi.isEnded());
    }

}
