package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Dto.HistoricTaskInstanceDto;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryTaskServiceImpl implements IHistoryTaskService {

    @Autowired
    private HistoryService historyService;

    @Override
    public List<HistoricTaskInstanceDto> getAllHistoricTasksByProcessInstanceId(String processInstanceId) {
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        // Convert HistoricTaskInstance objects to HistoricTaskInstanceDto objects
        return historicTasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HistoricTaskInstanceDto convertToDTO(HistoricTaskInstance historicTaskInstance) {
        HistoricTaskInstanceDto dto = new HistoricTaskInstanceDto();
        dto.setId(historicTaskInstance.getId());
        dto.setProcessInstanceId(historicTaskInstance.getProcessInstanceId());
        dto.setProcessDefinitionId(historicTaskInstance.getProcessDefinitionId());
        dto.setStartTime(historicTaskInstance.getStartTime());
        dto.setEndTime(historicTaskInstance.getEndTime());
        dto.setDurationInMillis(historicTaskInstance.getDurationInMillis());
        dto.setDeleteReason(historicTaskInstance.getDeleteReason());
        dto.setExecutionId(historicTaskInstance.getExecutionId());
        dto.setName(historicTaskInstance.getName());
        dto.setParentTaskId(historicTaskInstance.getParentTaskId());
        dto.setDescription(historicTaskInstance.getDescription());
        dto.setOwner(historicTaskInstance.getOwner());
        dto.setAssignee(historicTaskInstance.getAssignee());
        dto.setTaskDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
        dto.setFormKey(historicTaskInstance.getFormKey());
        dto.setPriority(historicTaskInstance.getPriority());
        dto.setDueDate(String.valueOf(historicTaskInstance.getDueDate()));
        dto.setClaimTime(String.valueOf(historicTaskInstance.getClaimTime()));
        dto.setCategory(historicTaskInstance.getCategory());
        dto.setTenantId(historicTaskInstance.getTenantId());
        dto.setBusinessKey(historicTaskInstance.getBusinessKey());
        dto.setCreateTime(historicTaskInstance.getCreateTime().toString()); // Convert LocalDateTime to String

        return dto;
    }
}

