package com.backendservice.EDU_Connect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String fileUrl;

    // Many resources belong to one module
    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    @JsonIgnore
    private Module module;

    // Many resources are uploaded by one teacher
    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    @JsonBackReference
    private User uploadedBy;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public Module getModule() {
        return module;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }
}
