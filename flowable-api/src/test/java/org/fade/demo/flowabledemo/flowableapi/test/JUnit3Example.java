package org.fade.demo.flowabledemo.flowableapi.test;

import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.DeploymentId;
import org.flowable.engine.test.FlowableTestCase;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JUnit3Example extends FlowableTestCase {

    private static final Logger logger = LoggerFactory.getLogger(JUnit3Example.class);

    @Override
    public String getConfigurationResource() {
        return "flowable.custom.cfg.xml";
    }

    @Deployment(resources = {"holiday-request.bpmn20.xml"})
    public void test() {
//    void test(@DeploymentId String deploymentId) {
        logger.info("Deployment id is " + deploymentId);
        runtimeService.startProcessInstanceByKey("holidayRequest");
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("Task's name is " + task.getName());
    }

}
