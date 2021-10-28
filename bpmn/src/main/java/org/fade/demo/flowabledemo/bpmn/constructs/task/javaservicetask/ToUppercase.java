package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fade
 * @date 2021/10/28
 */
public class ToUppercase implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ToUppercase.class);

    @Override
    public void execute(DelegateExecution execution) {
        String var = (String) execution.getVariable("input");
        logger.info("input:" + var);
        var = var.toUpperCase();
        execution.setVariable("input", var);
        logger.info("ToUppercase执行完成");
    }

}
