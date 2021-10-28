package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.DelegateHelper;
import org.flowable.engine.impl.delegate.ActivityBehavior;

/**
 * @author fade
 * @date 2021/10/28
 */
public class ThrowsExceptionBehavior implements ActivityBehavior {

    @Override
    public void execute(DelegateExecution execution) {
        String var = (String) execution.getVariable("var");
        String sequenceFlowToTake = null;
        try {
            executeLogic(var);
            sequenceFlowToTake = "no-exception";
        } catch (Exception e) {
            sequenceFlowToTake = "exception";
        }
        DelegateHelper.leaveDelegate(execution, sequenceFlowToTake);
    }

    private void executeLogic(String value) {
        if ("throw-exception".equals(value)) {
            throw new RuntimeException();
        }
    }

}
