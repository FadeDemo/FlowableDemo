package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.FlowableFutureJavaDelegate;

/**
 * @author fade
 * @date 2021/10/29
 */
public class FlowableFutureJavaDelegateExample implements FlowableFutureJavaDelegate<String, String> {

    @Override
    public String prepareExecutionData(DelegateExecution execution) {
        return (String) execution.getVariable("input");
    }

    @Override
    public String execute(String inputData) {
        return inputData.toUpperCase();
    }

    @Override
    public void afterExecution(DelegateExecution execution, String executionData) {
        execution.setVariable("input", executionData);
    }

}
