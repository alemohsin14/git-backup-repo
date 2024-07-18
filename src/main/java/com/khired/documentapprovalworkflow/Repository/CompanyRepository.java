package com.khired.documentapprovalworkflow.Repository;

import com.khired.documentapprovalworkflow.Model.Company;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findById(String id);
    void deleteById(String id);
    boolean existsById(String id);
    boolean existsByName(String name);
    boolean existsByApiUrl(String apiUrl);
    @Query("SELECT c.apiUrl FROM Company c WHERE c.id = :companyId")
    String findApiUrlByCompanyId(@Param("companyId") String companyId);
}
