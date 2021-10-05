package org.fade.demo.flowabledemo.springintegration.test;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.SpringFlowableTestCase;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:flowable.cfg.xml")
public class TransactionTest extends SpringFlowableTestCase {

    private static final Logger logger = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    private RepositoryService repositoryService;

    @Test
    @Deployment(resources = {"holiday-request.bpmn20.xml"})
    public void test() {
        // fixme debug调试有正常语句输出，直接run或者debug返回后无法查找调试记录
        // fixme 已尝试@FlowableTest 和 extends SpringFlowableTestCase 皆无法实现预期效果，后者甚至会报错
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();
        String deploymentId = deployment.getId();
        logger.info("Deployment id is " + deploymentId);
    }

}
