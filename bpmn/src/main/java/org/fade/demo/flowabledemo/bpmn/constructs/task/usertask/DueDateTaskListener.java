package org.fade.demo.flowabledemo.bpmn.constructs.task.usertask;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.Date;

/**
 * @author fade
 * @date 2021/10/23
 */
public class DueDateTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        Date dueDate = new Date();
        dueDate.setTime(dueDate.getTime() + 20*60*1000L);
        // 如果已经通过flowable:dueDate设置到期时间，则会覆盖
        delegateTask.setDueDate(dueDate);
    }

}
