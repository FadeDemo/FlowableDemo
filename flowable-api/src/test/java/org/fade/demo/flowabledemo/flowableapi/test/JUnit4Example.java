package org.fade.demo.flowabledemo.flowableapi.test;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableRule;
import org.flowable.task.api.Task;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JUnit4Example {

    private static final Logger logger = LoggerFactory.getLogger(JUnit4Example.class);

    @Rule
    public FlowableRule flowableRule = new FlowableRule("flowable.custom.cfg.xml");

    @Test
    @Deployment(resources = {"holiday-request.bpmn20.xml"})
    public void test() {
//    public void test(@DeploymentId String deploymentId) {
//        logger.info("Deployment id is " + deploymentId);
        RepositoryService repositoryService = flowableRule.getRepositoryService();
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeploymentQuery().singleResult();
        logger.info("Deployment id is " + deployment.getId());
        RuntimeService runtimeService = flowableRule.getRuntimeService();
        runtimeService.startProcessInstanceByKey("holidayRequest");
        TaskService taskService = flowableRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        logger.info("Task's name is " + task.getName());
    }

}
