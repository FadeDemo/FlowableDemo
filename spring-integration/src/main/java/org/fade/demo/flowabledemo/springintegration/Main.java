package org.fade.demo.flowabledemo.springintegration;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author fade
 * @date 2021/09/30
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("flowable.cfg.xml");
        RepositoryService repositoryService = applicationContext.getBean("repositoryService", RepositoryService.class);
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();
        Scanner scanner= new Scanner(System.in);
        System.out.println("Who are you?");
        String employee = scanner.nextLine();
        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());
        System.out.println("Why do you need them?");
        String description = scanner.nextLine();
        RuntimeService runtimeService = applicationContext.getBean("runtimeService", RuntimeService.class);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        TaskService taskService = applicationContext.getBean("taskService", TaskService.class);
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        logger.info("You have " + tasks.size() + " tasks:");
        for (int i=0; i<tasks.size(); i++) {
            logger.info((i+1) + ") " + tasks.get(i).getName());
        }
        System.out.println("Which task would you like to complete?");
        int taskIndex = Integer.valueOf(scanner.nextLine());
        Task task = tasks.get(taskIndex - 1);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        logger.info(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");
        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        variables = new HashMap<String, Object>();
        variables.put("approved", approved);
        taskService.complete(task.getId(), variables);

        List<Task> taskList = taskService.createTaskQuery().taskAssignee(employee).list();
        for (int i=0; i<taskList.size(); i++) {
            if (taskList.get(i).getTaskDefinitionKey().equals("holidayApprovedTask")) {
                logger.info("employee " + employee + ", your holiday request had been approved");
            }
            taskService.complete(taskList.get(i).getId());
        }
        HistoryService historyService = applicationContext.getBean("historyService", HistoryService.class);
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
