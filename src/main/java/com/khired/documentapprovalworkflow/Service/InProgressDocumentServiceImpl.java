package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.DocumentStorage;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.aspectj.bridge.ISourceLocation;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InProgressDocumentServiceImpl implements IInProgressDocumentService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private CompanyServiceImpl companyServiceImpl;

    @Autowired
    private DocumentStorageServiceImpl documentStorageServiceImpl;

    @Override
    public Map<String, Object> getProcessDetails(String processId) {
        Map<String, Object> processDetails = new HashMap<>();

        // Get process instance details
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .singleResult();

        if (processInstance != null) {
            processDetails.put("processInstanceId", processInstance.getId());
            processDetails.put("processDefinitionId", processInstance.getProcessDefinitionId());
            processDetails.put("isEnded", processInstance.isEnded());
            processDetails.put("isSuspended", processInstance.isSuspended());

            // Get current task details
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processId)
                    .singleResult();

            if (task != null) {
                processDetails.put("currentTaskId", task.getId());
                processDetails.put("currentTaskName", task.getName());
                processDetails.put("currentTaskAssignee", task.getAssignee());
            } else {
                processDetails.put("currentTask", "No active task");
            }

            // Get historic activity instances
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processId)
                    .orderByHistoricActivityInstanceStartTime()
                    .asc()
                    .list();

            List<Map<String, Object>> activities = historicActivityInstances.stream().map(activity -> {
                Map<String, Object> activityDetails = new HashMap<>();
                activityDetails.put("activityId", activity.getActivityId());
                activityDetails.put("activityName", activity.getActivityName());
                activityDetails.put("activityType", activity.getActivityType());
                activityDetails.put("startTime", activity.getStartTime());
                activityDetails.put("endTime", activity.getEndTime());
                activityDetails.put("assignee", activity.getAssignee());
                return activityDetails;
            }).collect(Collectors.toList());

            processDetails.put("activities", activities);
        } else {
            processDetails.put("error", "Process instance not found");
        }

        return processDetails;
    }

    @Override
    public String completeTask(String processId, String taskName, boolean docApprovalResult, String operatorId) {
        MessageUtil logger = null;
        try {
            // Fetch document storage by process instance id
            DocumentStorage documentStorage = documentStorageServiceImpl.getDocumentByProcessInstanceId(processId);

            // Check if documentStorage is null or companyId is null/empty
            if (documentStorage == null || documentStorage.getCompanyId() == null || documentStorage.getId() == null) {
                throw new IllegalArgumentException("Document storage fetch returned null or incorrect data.");
            }

            // Retrieve companyId and docToken
            String companyId = documentStorage.getCompanyId();
            String docToken = documentStorage.getId();

            // Fetch apiUrl using companyId
            String apiUrl = companyServiceImpl.getApiUrlByCompanyId(companyId);
            if (apiUrl == null) {
                throw new IllegalStateException("Company API URL fetch returned null for companyId: " + companyId);
            }

            // Set variables for task completion
            Map<String, Object> variables = new HashMap<>();
            variables.put("companyId", companyId);
            variables.put("apiUrl", apiUrl);
            variables.put("docToken", docToken);
            variables.put("docApprovalResult", docApprovalResult);

            // Query for the current task in the process instance
            Task task = taskService.createTaskQuery()
                    .processInstanceId(processId)
                    .singleResult();

            // Check if task is found and matches the provided taskName
            if (task != null && taskName.equals(task.getName())) {
                task.setAssignee(operatorId);
                taskService.saveTask(task);
                taskService.complete(task.getId(), variables);
                documentStorageServiceImpl.removeDocumentByProcessId(processId);
                return "Task '" + taskName + "' completed successfully.";
            } else if (task != null) {
                throw new IllegalArgumentException("Task name does not match. Current task name is '" + task.getName() + "'.");
            } else {
                throw new IllegalStateException("No active task found for the process instance.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Log the exception
            logger.error("Error completing task: " + e.getMessage(), (ISourceLocation) e);
            // Optionally rethrow or handle the exception based on your application's requirements
            return "Error completing task: " + e.getMessage();
        } catch (Exception e) {
            // Log unexpected exceptions
            logger.error("Unexpected error completing task", (ISourceLocation) e);
            return "Unexpected error completing task. Please contact support.";
        }
    }

}
