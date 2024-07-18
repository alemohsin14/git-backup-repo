package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Dto.HistoricTaskInstanceDto;

import java.util.List;

public interface IHistoryTaskService {
    List<HistoricTaskInstanceDto> getAllHistoricTasksByProcessInstanceId(String processInstanceId);
}
