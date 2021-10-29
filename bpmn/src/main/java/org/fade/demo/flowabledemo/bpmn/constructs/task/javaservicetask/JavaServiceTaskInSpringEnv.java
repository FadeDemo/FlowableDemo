package org.fade.demo.flowabledemo.bpmn.constructs.task.javaservicetask;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fade
 * @date 2021/10/29
 */
public class JavaServiceTaskInSpringEnv {

    private static final Logger logger = LoggerFactory.getLogger(JavaServiceTaskInSpringEnv.class);

    public static void main(String[] args) {
        // 获取Spring上下文
        ApplicationContext context = new ClassPathXmlApplicationContext("constructs/task/flowable.cfg.xml");
        // 部署流程实例
        RepositoryService repositoryService = context.getBean("repositoryService", RepositoryService.class);
        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("constructs/task/javaServiceTask.bpmn20.xml")
                // 测试flowable:expression调用的方法返回CompletableFuture<?>
                .addClasspathResource("constructs/task/futureJavaDelegate.bpmn20.xml")
                .deploy();
        // 启动流程实例
        RuntimeService runtimeService = context.getBean("runtimeService", RuntimeService.class);
        Map<String, Object> variables = new HashMap<>(16);
        variables.put("input", "abcDEFghi");
//        ProcessInstance pi = runtimeService.startProcessInstanceByKey("javaServiceTask", variables);
        // 测试flowable:expression调用的方法返回CompletableFuture<?>
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("futureJavaDelegate", variables);
        // 查询流程变量
        HistoryService historyService = context.getBean("historyService", HistoryService.class);
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(pi.getProcessInstanceId()).list();
        list.forEach(x -> logger.info(x.getVariableName() + ":" + x.getValue()));
    }

}
