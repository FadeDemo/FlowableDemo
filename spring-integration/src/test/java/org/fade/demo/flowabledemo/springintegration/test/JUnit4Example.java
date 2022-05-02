package org.fade.demo.flowabledemo.springintegration.test;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableRule;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable.cfg.xml")
@ActiveProfiles({"test"})
public class JUnit4Example {

    private static final Logger logger = LoggerFactory.getLogger(JUnit4Example.class);

    @Autowired
    @Rule
    public FlowableRule flowableRule;

    @Autowired
    private RepositoryService repositoryService1;

    @Autowired
    private ProcessEngine processEngine;

    @After
    public void closeProcessEngine() {
        // Required, since all the other tests seem to do a specific drop on the end
        processEngine.close();
    }

    @Test
    @Deployment(resources = {"holiday-request.bpmn20.xml"})
    public void test() {
        RepositoryService repositoryService2 = flowableRule.getRepositoryService();
        org.flowable.engine.repository.Deployment deployment = repositoryService2.createDeploymentQuery().singleResult();
        logger.info("Deployment id is " + deployment.getId());
        logger.info("RuntimeService from autowired is the same reference as RuntimeService from FlowableRule: " + (repositoryService1==repositoryService2));
    }

}
