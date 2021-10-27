package org.fade.demo.flowabledemo.bpmn.constructs.task.scripttask;

import org.fade.demo.flowabledemo.bpmn.util.DBUtil;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fade
 * @date 2021/10/27
 */
public class ScriptTask {

    private static final Logger logger = LoggerFactory.getLogger(ScriptTask.class);

    public static void main(String[] args) {
        // 获取流程引擎配置
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(DBUtil.getMemoryJdbcUrl())
                .setJdbcUsername(DBUtil.getUsername())
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 获取流程引擎
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("constructs/task/scriptTask.bpmn20.xml")
                .deploy();
        // 启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<>(16);
        // 测试脚本中使用流程变量
        // 测试JUEL
//        variables.put("count", 999);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("scriptTask", variables);
        // 查询脚本任务
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(pi.getProcessInstanceId()).list();
        list.forEach(x -> logger.info(x.getVariableName() + ":" + x.getValue()));
    }

}
