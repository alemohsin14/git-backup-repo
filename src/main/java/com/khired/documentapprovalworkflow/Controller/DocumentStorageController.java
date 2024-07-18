package com.khired.documentapprovalworkflow.Controller;

import com.khired.documentapprovalworkflow.Service.IDocumentStorageService;
import com.khired.documentapprovalworkflow.Model.DocumentStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing document storage operations.
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentStorageController {

    @Autowired
    private IDocumentStorageService IDocumentStorageService;

    /**
     * Endpoint to retrieve all stored documents.
     *
     * @return List of DocumentStorage objects representing all stored documents.
     */
    @GetMapping("/")
    public List<DocumentStorage> getAllDocuments() {
        return IDocumentStorageService.getAllDocuments();
    }

    /**
     * Endpoint to remove a document by its process instance ID.
     *
     * @param processInstanceId The ID of the process instance associated with the document to be removed.
     */
    @DeleteMapping("/{processInstanceId}")
    public void removeDocumentByProcessId(@PathVariable String processInstanceId) {
        IDocumentStorageService.removeDocumentByProcessId(processInstanceId);
    }
}
