package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fade
 * @date 2021/10/28
 */
public class LongRunningJavaDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(LongRunningJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String input = (String) execution.getVariable("input");
        logger.info("input:" + input);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        input = "done";
        execution.setVariable("input", input);
        logger.info("LongRunningJavaDelegate执行完成");
    }

}
