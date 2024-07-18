package com.khired.documentapprovalworkflow.Controller;

import com.khired.documentapprovalworkflow.Service.ICompanyService;
import com.khired.documentapprovalworkflow.Service.IDocumentReceivedService;
import com.khired.documentapprovalworkflow.Model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller class for managing document verification processes.
 */
@RestController
@RequestMapping("/api/verification")
public class DocumentReceivedController {

    @Autowired
    private IDocumentReceivedService IDocumentReceivedService;

    @Autowired
    private ICompanyService ICompanyService;

    @Value("${file.upload.dir}")
    private String uploadDir; // Defined in application.properties

    /**
     * Endpoint to start a document verification process.
     *
     * @param file      The document file to be verified (multipart/form-data).
     * @param companyId The ID of the company initiating the verification process.
     * @return ResponseEntity with HTTP status 200 OK and a success message if the process is started successfully, or status 400 Bad Request or 404 Not Found for invalid requests.
     */
    @PostMapping("/startProcess")
    public ResponseEntity<String> startProcessInstance(
            @RequestParam("file") MultipartFile file,
            @RequestParam("companyId") String companyId) {

        // Check if the company exists
        Optional<Company> companyOptional = ICompanyService.getCompanyById(companyId);
        if (companyOptional.isEmpty()) {
            // Return 404 Not Found response if company does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Company with ID " + companyId + " does not exist.");
        }

        // Check if the file is empty or not present
        if (file.isEmpty()) {
            // Return 400 Bad Request response if file is empty
            return ResponseEntity.badRequest().body("Uploaded file is empty.");
        }

        // Set the name of the process instance to a UUID
        String processInstanceNameToken = UUID.randomUUID().toString();

        // Save the file and get the file URL
        String fileUrl = saveFile(file, processInstanceNameToken);

        // Start the process with the file URL, process instance name, and companyId
        IDocumentReceivedService.startProcess(processInstanceNameToken, fileUrl, companyId);

        // Return 200 OK response with success message
        return ResponseEntity.ok("Process started with name: " + processInstanceNameToken);
    }

    // Function to save file with a prefix and return its URL
    private String saveFile(MultipartFile file, String prefix) {
        String fileName = file.getOriginalFilename();
        String newFileName = prefix + "_" + fileName; // Add prefix to the original file name
        String filePath = uploadDir + newFileName; // Assuming uploadDir is defined elsewhere

        try {
            file.transferTo(new File(filePath)); // Save the file with the new name
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file: " + fileName);
        }

        return filePath; // Return the full path to the saved file
    }
}
