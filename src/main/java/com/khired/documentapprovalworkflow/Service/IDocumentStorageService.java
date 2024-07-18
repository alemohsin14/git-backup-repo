package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.DocumentStorage;

import java.util.List;

public interface IDocumentStorageService {
    List<DocumentStorage> getAllDocuments();
    List<DocumentStorage> findByCompanyId(String companyId);
    void removeDocumentByProcessId(String processInstanceId);
    String getIdByProcessInstanceId(String processInstanceId);
    DocumentStorage getDocumentByProcessInstanceId(String processId);
}
