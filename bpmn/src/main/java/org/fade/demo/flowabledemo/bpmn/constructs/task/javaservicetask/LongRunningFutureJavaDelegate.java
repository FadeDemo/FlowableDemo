package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.common.engine.api.async.AsyncTaskInvoker;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.FutureJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author fade
 * @date 2021/10/28
 */
public class LongRunningFutureJavaDelegate implements FutureJavaDelegate<String> {

    private static final Logger logger = LoggerFactory.getLogger(LongRunningFutureJavaDelegate.class);

    @Override
    public CompletableFuture<String> execute(DelegateExecution execution, AsyncTaskInvoker taskInvoker) {
        String input = (String) execution.getVariable("input");
        logger.info("input:" + input);
        return taskInvoker.submit(() -> {
            Thread.sleep(60000);
            return "done";
        });
    }

    @Override
    public void afterExecution(DelegateExecution execution, String executionData) {
        execution.setVariable("input", executionData);
        logger.info("LongRunningFutureJavaDelegate执行完成");
    }

}
