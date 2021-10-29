package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author fade
 * @date 2021/10/28
 */
// 测试通过flowable:delegateExpression调用Java逻辑(流程变量的方式)
public class ToUppercase implements JavaDelegate, Serializable {
//public class ToUppercase implements JavaDelegate {

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
