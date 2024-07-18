package com.khired.documentapprovalworkflow.Controller;

import com.khired.documentapprovalworkflow.Dto.HistoricTaskInstanceDto;
import com.khired.documentapprovalworkflow.Service.IHistoryTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for managing historic task instance operations.
 */
@RestController
@RequestMapping("/api")
public class HistoryTaskController {

    @Autowired
    private IHistoryTaskService IHistoryTaskService;

    /**
     * Endpoint to retrieve historic task instances by process instance ID.
     *
     * @param request A map containing request parameters. Requires "processInstanceId" key with the process instance ID.
     * @return List of HistoricTaskInstanceDto objects representing historic task instances.
     */
    @PostMapping("/historyTasks")
    public List<HistoricTaskInstanceDto> getHistoricTasks(@RequestBody Map<String, String> request) {
        String processInstanceId = request.get("processInstanceId");
        return IHistoryTaskService.getAllHistoricTasksByProcessInstanceId(processInstanceId);
    }
}
