package org.fade.demo.flowabledemo.flowableapi;

import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DiagramLayout;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author fade
 * @date 2021/09/28
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = cfg.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();
        List<Deployment> query = repositoryService.createDeploymentQuery().orderByDeploymentId().asc().list();
        logger.info("Found " + query.size() + " deployment");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        logger.info("Found process definition : " + processDefinition.getName());
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());
        repositoryService.activateProcessDefinitionById(processDefinition.getId());
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        ProcessDiagramGenerator processDiagramGenerator = cfg.getProcessDiagramGenerator();
        BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
        bpmnAutoLayout.setTaskWidth(200);
        bpmnAutoLayout.setTaskHeight(70);
        bpmnAutoLayout.execute();
        InputStream processDiagramInput = processDiagramGenerator.generateDiagram(bpmnModel, "png", new ArrayList<>(), 1.0, true);
//        InputStream processDiagramInput = new BufferedInputStream(repositoryService.getProcessDiagram(processDefinition.getId()));
        InputStream resourceInput = new BufferedInputStream(repositoryService.getResourceAsStream(deployment.getId(), "holiday-request.bpmn20.xml"));
        OutputStream processDiagramOutput = null;
        OutputStream resourceOutput = null;
        String userDir = System.getProperty("user.dir") + "\\flowable-api";
        try {
            processDiagramOutput = new BufferedOutputStream(new FileOutputStream(userDir + "\\test\\processDiagram.png"));
            resourceOutput = new BufferedOutputStream(new FileOutputStream(userDir + "\\test\\holiday-request.bpmn20.xml"));
            byte[] buffer = new byte[10240];
            int length = 0;
            while ((length = processDiagramInput.read(buffer)) != -1) {
                processDiagramOutput.write(buffer, 0, length);
            }
            processDiagramOutput.flush();
            buffer = new byte[10240];
//            int length = 0;
            length = 0;
            while ((length = resourceInput.read(buffer)) != -1) {
                resourceOutput.write(buffer, 0, length);
            }
            resourceOutput.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (processDiagramOutput != null) {
                try {
                    processDiagramOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resourceOutput != null) {
                try {
                    resourceOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                processDiagramInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                resourceInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*********************************************************/

        Scanner scanner= new Scanner(System.in);

        System.out.println("Who are you?");
        String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        System.out.println("Why do you need them?");
        String description = scanner.nextLine();

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        // 执行流程实例
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        // 查询流程实例
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        logger.info("Found " + processInstances.size() + " process instances");
        // 查询执行
        List<Execution> executions = runtimeService.createExecutionQuery().list();
        logger.info("Found " + executions.size() + " executions");
        TaskService taskService = processEngine.getTaskService();
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
        // FIXME 输入流为空
        InputStream runtimeProcessDiagramInput = repositoryService.getProcessDiagram(processDefinition.getId());
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(employee).list();
        for (int i=0; i<taskList.size(); i++) {
            if (taskList.get(i).getTaskDefinitionKey().equals("holidayApprovedTask")) {
                logger.info("employee " + employee + ", your holiday request had been approved");
            }
            taskService.complete(taskList.get(i).getId());
        }
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
