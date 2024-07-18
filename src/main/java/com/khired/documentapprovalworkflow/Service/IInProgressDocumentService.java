package com.khired.documentapprovalworkflow.Service;

import java.util.Map;

public interface IInProgressDocumentService {
    Map<String, Object> getProcessDetails(String processId);
    String completeTask(String processId, String taskName, boolean docApprovalResult, String operatorId);
}
