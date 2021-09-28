package org.fade.demo.flowabledemo.configuration;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fade
 * @date 2021/09/24
 */
public class SendRejectionMail implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(SendRejectionMail.class);

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("Sending rejection email for employee "
                + execution.getVariable("employee"));
    }

}
