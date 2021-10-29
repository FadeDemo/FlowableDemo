package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

/**
 * @author fade
 * @date 2021/10/29
 */
//测试通过flowable:expression调用Java逻辑
// 流程变量方式
public class Normal implements Serializable {
//public class Normal {

    private static final Logger logger = LoggerFactory.getLogger(Normal.class);

    public void execute(DelegateExecution execution) {
        String var = (String) execution.getVariable("input");
        logger.info("input:" + var);
        var = var.toUpperCase();
        execution.setVariable("input", var);
        logger.info("Normal ToUppercase执行完成");
    }

    // fixme 无法输出调用日志（无法显示出被调用的效果）
    public CompletableFuture<Void> longTimeExecute(DelegateExecution execution) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            execute(execution);
            return null;
        });
    }

}
