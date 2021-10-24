package org.fade.demo.flowabledemo.bpmn.constructs.task.usertask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author fade
 * @date 2021/10/24
 */
public class UserTaskInSpringEnv {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("constructs/task/flowable.cfg.xml");

    }

}
