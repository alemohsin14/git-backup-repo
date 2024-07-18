package com.khired.documentapprovalworkflow.Service;

import com.khired.documentapprovalworkflow.Model.Company;
import com.khired.documentapprovalworkflow.Repository.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements ICompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> getCompanyById(String id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public void deleteCompany(String id) {
        companyRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return companyRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }

    @Override
    public boolean existsByApiUrl(String apiUrl) {
        return companyRepository.existsByApiUrl(apiUrl);
    }

    @Override
    public String getApiUrlByCompanyId(String companyId) {
        String apiUrl = companyRepository.findApiUrlByCompanyId(companyId);
        if (apiUrl == null) {
            throw new IllegalArgumentException("No API URL found for companyId: " + companyId);
        }
        return apiUrl;
    }
}
