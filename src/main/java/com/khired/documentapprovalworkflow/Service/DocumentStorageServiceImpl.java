package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.DocumentStorage;
import com.khired.documentapprovalworkflow.Repository.DocumentStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentStorageServiceImpl implements IDocumentStorageService {
    @Autowired
    private DocumentStorageRepository documentStorageRepository;

    @Override
    public List<DocumentStorage> getAllDocuments() {
        return documentStorageRepository.findAll();
    }

    @Override
    public List<DocumentStorage> findByCompanyId(String companyId) {
        return documentStorageRepository.findByCompanyId(companyId);
    }

    @Override
    public void removeDocumentByProcessId(String processInstanceId) {
        DocumentStorage document = documentStorageRepository.findByProcessInstanceOfDoc(processInstanceId);
        if (document != null) {
            documentStorageRepository.delete(document);
        } else {
            throw new IllegalArgumentException("Document not found for processInstanceId: " + processInstanceId);
        }
    }

    @Override
    public String getIdByProcessInstanceId(String processInstanceId) {
        DocumentStorage documentStorage = documentStorageRepository.findByProcessInstanceOfDoc(processInstanceId);
        return documentStorage.getId();
    }

    @Override
    public DocumentStorage getDocumentByProcessInstanceId(String processInstanceId) {
        return documentStorageRepository.findByProcessInstanceOfDoc(processInstanceId);
    }
}
