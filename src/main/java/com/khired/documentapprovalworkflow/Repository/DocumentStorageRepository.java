package com.khired.documentapprovalworkflow.Repository;

import com.khired.documentapprovalworkflow.Model.DocumentStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentStorageRepository extends JpaRepository<DocumentStorage,Long> {
    List<DocumentStorage> findByCompanyId(String companyId);
    DocumentStorage findByProcessInstanceOfDoc(String processInstanceOfDoc);
}
