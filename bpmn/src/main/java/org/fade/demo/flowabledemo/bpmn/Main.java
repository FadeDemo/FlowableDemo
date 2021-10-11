package org.fade.demo.flowabledemo.bpmn;

import cn.hutool.core.lang.Assert;
import cn.hutool.setting.dialect.Props;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author fade
 * @date 2021/10/09
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Props props = new Props("db.properties");
        String jdbcUrl = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        Assert.notBlank(jdbcUrl, "jdbcUrl is illegal");
        Assert.notBlank(username, "username is illegal");
        Assert.notNull(password, "password can not be null");
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl(jdbcUrl)
                .setJdbcUsername(username)
                .setJdbcPassword(password)
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("FinancialReportProcess.bpmn20.xml")
                .deploy();
        // 启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("financialReport");
        // 获取任务列表
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        logger.info("Accountancy group has " + tasks.size() + " task(s)");
        // 申领任务
        taskService.claim(tasks.get(0).getId(), "kermit");
        // 查询任务执行人为kermit的任务列表
        List<Task> kermitTasks = taskService.createTaskQuery().taskAssignee("kermit").list();
        logger.info("Kermit has " + kermitTasks.size() + " task(s)");
    }

}
