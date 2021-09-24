package org.fade.demo.flowabledemo.holidayrequest;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fade
 * @date 2021/09/24
 */
public class CallExternalSystemDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(CallExternalSystemDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        logger.info("Calling the external system for employee "
                + execution.getVariable("employee"));
    }

}
