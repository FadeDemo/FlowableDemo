package org.fade.demo.flowabledemo.flowableapi.test;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.ConfigurationResource;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.DeploymentId;
import org.flowable.engine.test.FlowableTest;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FlowableTest
@ConfigurationResource("flowable.custom.cfg.xml")
public class JUnit5Example {

    private static final Logger logger = LoggerFactory.getLogger(JUnit5Example.class);

    private ProcessEngine processEngine;
    private RuntimeService runtimeService;
    private TaskService taskService;

    @BeforeEach
    void setUp(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.runtimeService = processEngine.getRuntimeService();
        this.taskService = processEngine.getTaskService();
    }

    @Test
    @Deployment(resources = {"holiday-request.bpmn20.xml"})
//    void testSimpleProcess() {
    void test(@DeploymentId String deploymentId) {
        logger.info("Deployment id is " + deploymentId);
        runtimeService.startProcessInstanceByKey("holidayRequest");
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("Task's name is " + task.getName());
    }

}
