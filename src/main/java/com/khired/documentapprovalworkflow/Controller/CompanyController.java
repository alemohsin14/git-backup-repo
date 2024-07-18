package com.khired.documentapprovalworkflow.Controller;

import com.khired.documentapprovalworkflow.Service.ICompanyService;
import com.khired.documentapprovalworkflow.Model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing operations related to companies.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private ICompanyService ICompanyService;

    /**
     * Retrieve all companies.
     *
     * @return List of Company objects representing all companies.
     */
    @GetMapping("/")
    public List<Company> getAllCompanies() {
        return ICompanyService.getAllCompanies();
    }

    /**
     * Retrieve a company by its ID.
     *
     * @param id The ID of the company to retrieve.
     * @return ResponseEntity containing the retrieved Company object if found, or 404 Not Found if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        Optional<Company> company = ICompanyService.getCompanyById(id);
        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new company.
     *
     * @param company The Company object to create. Must be a validated and non-existing company name and API URL.
     * @return ResponseEntity with the created Company object and HTTP status 201 Created, or status 409 Conflict if a company with the same name or API URL already exists.
     */
    @PostMapping("/add")
    public ResponseEntity<?> createCompany(@Validated @RequestBody Company company) {
        // Check if company with the same name or apiUrl already exists
        if (ICompanyService.existsByName(company.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Company name already exists");
        }

        if (ICompanyService.existsByApiUrl(company.getApiUrl())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("API URL already exists");
        }

        // Save the company if not exists
        Company savedCompany = ICompanyService.saveCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    /**
     * Update an existing company by ID.
     *
     * @param id             The ID of the company to update.
     * @param companyDetails Updated Company details. Must be a validated and non-conflicting API URL.
     * @return ResponseEntity with the updated Company object and HTTP status 200 OK, or status 404 Not Found if the company does not exist, or status 409 Conflict if the updated API URL conflicts with another company.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable String id, @Validated @RequestBody Company companyDetails) {
        Optional<Company> companyOptional = ICompanyService.getCompanyById(id);

        if (companyOptional.isPresent()) {
            Company existingCompany = companyOptional.get();

            // Check if the updated apiUrl is already in use by another company
            if (!existingCompany.getApiUrl().equals(companyDetails.getApiUrl()) && ICompanyService.existsByApiUrl(companyDetails.getApiUrl())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Update company details
            existingCompany.setName(companyDetails.getName());
            existingCompany.setApiUrl(companyDetails.getApiUrl()); // Update apiUrl if necessary

            // Save and return updated company
            Company updatedCompany = ICompanyService.saveCompany(existingCompany);
            return ResponseEntity.ok(updatedCompany);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a company by its ID.
     *
     * @param id The ID of the company to delete.
     * @return ResponseEntity with HTTP status 200 OK and a success message if deletion is successful, or status 404 Not Found if the company does not exist.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable String id) {
        if (ICompanyService.existsById(id)) {
            ICompanyService.deleteCompany(id);
            return ResponseEntity.ok("Company deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
