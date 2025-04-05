package com.backendservice.EDU_Connect.payload.request;


import lombok.Data;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
public class ResourceDTO {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String uploadedByName; // Simplified to just the uploader's name
    private Integer semester;

    public ResourceDTO() {

    }
}


