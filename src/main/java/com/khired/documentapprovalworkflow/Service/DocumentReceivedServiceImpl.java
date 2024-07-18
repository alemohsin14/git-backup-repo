package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.DocumentStorage;
import com.khired.documentapprovalworkflow.Repository.DocumentStorageRepository;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentReceivedServiceImpl implements IDocumentReceivedService {

    @Autowired
    private RuntimeService runtimeService;

    @Value("${process.key}")
    private String processKey;

    @Autowired
    private DocumentStorageRepository storageRepository;

    @Override
    public void startProcess(String processInstanceNameToken, String fileUrl, String companyId) {

        // Start the process instance
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processKey);

        // Set the name of the process instance
        runtimeService.setProcessInstanceName(processInstance.getId(), processInstanceNameToken);

        // Save file URL and other process details to database (assuming entity and repository setup)
        saveFileDetailsToDatabase(processInstanceNameToken, processInstance.getId(), fileUrl, companyId);
        //processInstanceNameToken is that token sent to

        // Retrieve process instance details
        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();
        String processName = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult()
                .getName();

        // Log process instance details
        System.out.println("Started process instance:");
        System.out.println("ID: " + processInstanceId);
        System.out.println("Definition ID: " + processDefinitionId);
        System.out.println("Name: " + processName); // Use the name provided to setProcessInstanceName
    }

    private void saveFileDetailsToDatabase(String processInstanceNameToken, String orgProcessInstanceOfDoc, String fileUrl, String companyId) {
        System.out.println("Save this to database");
        System.out.println("Token " + processInstanceNameToken);
        System.out.println("fileUrl " + fileUrl);
        System.out.println("Original PID " + orgProcessInstanceOfDoc);
        System.out.println("CompanyId " + companyId);

        // Create a new DocumentStorage entity
        DocumentStorage documentStorage = new DocumentStorage();
        documentStorage.setId(processInstanceNameToken); // Set id to processInstanceNameToken
        documentStorage.setProcessInstanceOfDoc(orgProcessInstanceOfDoc);
        documentStorage.setDocUrl(fileUrl);
        documentStorage.setCompanyId(companyId);

        // Save to database using Spring Data JPA repository
        storageRepository.save(documentStorage);
    }
}
