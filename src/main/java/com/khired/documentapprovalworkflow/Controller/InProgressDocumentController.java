package com.khired.documentapprovalworkflow.Controller;

import com.khired.documentapprovalworkflow.Service.IDocumentStorageService;
import com.khired.documentapprovalworkflow.Service.IInProgressDocumentService;
import com.khired.documentapprovalworkflow.Model.DocumentStorage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for managing in-progress document operations by operators.
 */
@RestController
@RequestMapping("/api/operators")
public class InProgressDocumentController {

    @Autowired
    private IDocumentStorageService IDocumentStorageService;

    @Autowired
    private IInProgressDocumentService IInProgressDocumentService;

    /**
     * Retrieves all documents in progress for a specific company.
     *
     * @param companyId The ID of the company for which documents in progress are retrieved.
     * @return List of DocumentStorage objects representing documents in progress.
     */
    @GetMapping("/in_progress/{companyId}")
    public List<DocumentStorage> getDocumentsByCompanyId(@PathVariable String companyId) {
        return IDocumentStorageService.findByCompanyId(companyId);
    }

    /**
     * Retrieves details of a process by its ID.
     *
     * @param processId The ID of the process for which details are retrieved.
     * @return ResponseEntity containing a map of process details.
     */
    @GetMapping("/process/{processId}")
    public ResponseEntity<Map<String, Object>> getProcessDetailsByProcessId(@PathVariable String processId) {
        Map<String, Object> processDetails = IInProgressDocumentService.getProcessDetails(processId);
        return ResponseEntity.ok(processDetails);
    }

    /**
     * Completes a task if the process ID, task name, and operator ID match the provided criteria.
     *
     * @param requestBody A map containing "processId", "taskName", "operatorId", and "docApprovalResult" keys.
     * @return ResponseEntity with a success message upon task completion.
     * @throws IllegalArgumentException If one or more required parameters are null.
     */
    @Transactional
    @PostMapping("/completeTask")
    public ResponseEntity<String> completeTask(@RequestBody Map<String, String> requestBody) {
        String processId = requestBody.get("processId");
        String taskName = requestBody.get("taskName");
        String operatorId = requestBody.get("operatorId");

        boolean docApprovalResult = Boolean.parseBoolean(requestBody.get("docApprovalResult"));

        if (processId == null || taskName == null || operatorId == null) {
            throw new IllegalArgumentException("One or more parameters are null");
        }

        String responseMessage = IInProgressDocumentService.completeTask(processId, taskName, docApprovalResult, operatorId);
        return ResponseEntity.ok(responseMessage);
    }
}
