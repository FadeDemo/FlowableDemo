package org.fade.demo.flowabledemo.configuration;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.ProcessEngines;

/**
 * @author fade
 * @date 2021/09/26
 */
public class Main {

    public static void main(String[] args) {
        // 自动读取类路径下配置文件
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 手动指定配置文件
//        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable.cfg.xml");
//        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 指定配置文件和bean id
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("flowable.cfg.xml", "processEngineConfigurationTest")
                .buildProcessEngine();
        // 不使用配置文件
//        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
//                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
//                .setAsyncExecutorActivate(false)
//                .buildProcessEngine();
    }

}
