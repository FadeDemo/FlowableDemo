package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.delegate.MapBasedFlowableFutureJavaDelegate;
import org.flowable.engine.delegate.ReadOnlyDelegateExecution;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fade
 * @date 2021/10/29
 */
public class MapBasedFlowableFutureJavaDelegateExample implements MapBasedFlowableFutureJavaDelegate {

    @Override
    public Map<String, Object> execute(ReadOnlyDelegateExecution inputData) {
        String input = (String) inputData.getVariable("input");
        input = input.toUpperCase();
        HashMap<String, Object> result = new HashMap<>(16);
        result.put("input", input);
        return result;
    }

}
