package org.fade.demo.flowabledemo.springintegration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author fade
 * @date 2021/10/06
 */
public class ServiceTask {

    private String id;

    private static final Logger logger = LoggerFactory.getLogger(ServiceTask.class);

    public ServiceTask() {
        this.id = UUID.randomUUID().toString();
    }

    public void externalSystemCall(String employee) {
        logger.info(this.id + ": Calling the external system for employee "
                + employee);
    }

    public void sendRejectionMail(String employee) {
        logger.info(this.id + ": Sending rejection email for employee "
                + employee);
    }

}
