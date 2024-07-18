package com.khired.documentapprovalworkflow.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Setter
@Table(name = "document_storage")
@Getter
@Entity
public class DocumentStorage {

    @Id
    private String id;

    @NotNull
    private String processInstanceOfDoc;

    @NotNull
    private String docUrl;

    @NotNull
    private String companyId;
}
