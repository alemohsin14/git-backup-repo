package com.khired.documentapprovalworkflow.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity
@Table(name = "company")
@Getter
@Setter
public class Company {
    @Id
    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String apiUrl;

    @PrePersist
    public void generateUUID() {
        this.id = UUID.randomUUID().toString();
    }
}
