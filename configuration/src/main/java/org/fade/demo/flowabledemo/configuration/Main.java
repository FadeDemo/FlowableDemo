package org.fade.demo.flowabledemo.configuration;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author fade
 * @date 2021/09/26
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // 自动读取类路径下配置文件
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 手动指定配置文件
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("flowable.cfg.xml");
        ProcessEngine processEngine = cfg.buildProcessEngine();
        // 指定配置文件和bean id
//        ProcessEngine processEngine = ProcessEngineConfiguration
//                .createProcessEngineConfigurationFromResource("flowable.cfg.xml", "processEngineConfigurationTest")
//                .buildProcessEngine();
        // 不使用配置文件
//        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
//                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
//                .setAsyncExecutorActivate(false)
//                .buildProcessEngine();
        /*********************************************************/

        // 部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();
        // 验证流程定义是否存在
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        logger.info("Found process definition : " + processDefinition.getName());

        /*********************************************************/

        // 启动流程实例，添加监听器
        // 实例化流程变量
        Scanner scanner= new Scanner(System.in);

        System.out.println("Who are you?");
        String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        System.out.println("Why do you need them?");
        String description = scanner.nextLine();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<>(16);
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);
//        runtimeService.addEventListener(new MyEventListener());
//        runtimeService.addEventListener(new MyEventListener(), FlowableEngineEventType.ENTITY_CREATED);
        /*********************************************************/

        // 查询与完成任务
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        logger.info("You have " + tasks.size() + " tasks:");
        for (int i=0; i<tasks.size(); i++) {
            logger.info((i+1) + ") " + tasks.get(i).getName());
        }
        System.out.println("Which task would you like to complete?");
        int taskIndex = Integer.parseInt(scanner.nextLine());
        Task task = tasks.get(taskIndex - 1);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        logger.info(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");
        boolean approved = "y".equalsIgnoreCase(scanner.nextLine());
        variables = new HashMap<>(16);
        variables.put("approved", approved);
        taskService.complete(task.getId(), variables);

        List<Task> taskList = taskService.createTaskQuery().taskAssignee(employee).list();
        for (Task value : taskList) {
            if ("holidayApprovedTask".equals(value.getTaskDefinitionKey())) {
                logger.info("employee " + employee + ", your holiday request had been approved");
            }
            taskService.complete(value.getId());
        }

        /*********************************************************/

        // 使用历史数据
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            logger.info(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }
    }

}
