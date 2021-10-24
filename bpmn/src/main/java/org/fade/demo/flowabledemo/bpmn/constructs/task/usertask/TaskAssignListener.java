package org.fade.demo.flowabledemo.bpmn.constructs.task.usertask;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * @author fade
 * @date 2021/10/24
 */
public class TaskAssignListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // 直接分配给一个用户
//        delegateTask.setAssignee("kermit");
        // 作为候选任务分配
        delegateTask.addCandidateUser("kermit");
        delegateTask.addCandidateGroup("management");
    }

}
