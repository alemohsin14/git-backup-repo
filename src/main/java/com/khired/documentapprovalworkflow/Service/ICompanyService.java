package com.khired.documentapprovalworkflow.Service;


import com.khired.documentapprovalworkflow.Model.Company;

import java.util.List;
import java.util.Optional;

public interface ICompanyService {
    List<Company> getAllCompanies();
    Optional<Company> getCompanyById(String id);
    Company saveCompany(Company company);
    void deleteCompany(String id);
    boolean existsById(String id);
    boolean existsByName(String name);
    boolean existsByApiUrl(String apiUrl);
    String getApiUrlByCompanyId(String companyId);
}
