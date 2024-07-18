package com.khired.documentapprovalworkflow.Service;

public interface IDocumentReceivedService {
    void startProcess(String processInstanceNameToken, String fileUrl, String companyId);
}