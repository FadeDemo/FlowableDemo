package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.common.engine.api.async.AsyncTaskInvoker;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.FutureJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author fade
 * @date 2021/11/01
 */
public class FieldInjectDelegate implements FutureJavaDelegate<Object> {

    private static final Logger logger = LoggerFactory.getLogger(FieldInjectDelegate.class);

    private Expression text;

    // 测试无setter方法注入
//    public void setText(Expression text) {
//        this.text = text;
//    }

    @Override
    public CompletableFuture<Object> execute(DelegateExecution execution, AsyncTaskInvoker taskInvoker) {
        String input = (String) text.getValue(execution);
        return taskInvoker.submit(() -> input.toUpperCase());
//        return taskInvoker.submit(() -> {
//            String input = (String) text.getValue(execution);
//            return input.toUpperCase();
//        });
    }

    @Override
    public void afterExecution(DelegateExecution execution, Object executionData) {
        execution.setVariable("text", executionData);
    }

}
