package org.flowable;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class HolidayRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayRequest.class);
    private static RepositoryService repositoryService;
    private static RuntimeService runtimeService;
    private static TaskService taskService;
    private static HistoryService historyService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // build process engine
        ProcessEngine processEngine = buildProcessEngine();


        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();

        // deploy
//        deploy();

        // queryProcessDefinition
        ProcessDefinition processDefinition = queryProcessDefinition("holidayRequest");
        LOGGER.info("holidayRequest id = {}, name = {}}", processDefinition.getId(), processDefinition.getName());

        // init variables
        Map<String, Object> variables = inputVariables();

        // startProcessInstanceByKey
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        // query task by candidate group
        List<Task> taskList = queryTaskCandidateGroup("managers");
        LOGGER.info("You have " + taskList.size() + " taskList:");
        for (int i = 0; i < taskList.size(); i++) {
            LOGGER.info((i + 1) + ") " + taskList.get(i).getName());
        }

        // select task
        LOGGER.info("Which task would you like to complete?");
        int taskIndex = Integer.parseInt(scanner.nextLine());
        Task task = taskList.get(taskIndex - 1);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        LOGGER.info(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

        // complete
        boolean approved = scanner.nextLine().equalsIgnoreCase("y");
        variables = new HashMap<>();
        variables.put("approved", approved);
        taskService.complete(task.getId(), variables);

        // query history
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processDefinition.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();
        for (HistoricActivityInstance activity : activities) {
            LOGGER.info(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }

    }

    /**
     * 构建工作流引擎
     */
    private static ProcessEngine buildProcessEngine() {
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://localhost:3306/flowable")
                .setJdbcUsername("root")
                .setJdbcPassword("1111")
                .setJdbcDriver("com.mysql.jdbc.Driver")
                .setDatabaseSchemaUpdate(AbstractEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngineConfiguration.setEventListeners(Collections.singletonList(new MyEventListener()));
        return processEngineConfiguration.buildProcessEngine();
    }

    /**
     * 输入启动参数
     */
    private static Map<String, Object> inputVariables() {


        LOGGER.info("Who are you?");
        String employee = scanner.nextLine();

        LOGGER.info("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        LOGGER.info("Why do you need them?");
        String description = scanner.nextLine();

        Map<String, Object> variables = new HashMap<>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);
        return variables;
    }

    /**
     * 根据组名查询任务
     */
    private static List<Task> queryTaskCandidateGroup(String candidateGroup) {
        return taskService.createTaskQuery().taskCandidateGroup(candidateGroup).list();
    }

    /**
     * 部署
     */
    private static void deploy() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();

        Deployment deployment = repositoryService.createDeploymentQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        LOGGER.info("Found process definition : " + deployment);
    }

    /**
     * 查询流程定义
     */
    private static ProcessDefinition queryProcessDefinition(String key) {
        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .latestVersion()
                .singleResult();
    }
}
