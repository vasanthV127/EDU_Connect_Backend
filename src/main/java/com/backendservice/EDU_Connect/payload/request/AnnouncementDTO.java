package com.backendservice.EDU_Connect.payload.request;


import lombok.Data;

@Data
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String content;
    private String datePosted;
    private Long moduleId; // Only include module ID, not the full object
    private Long postedById; // Only include user ID
    private String postedByName; // Optional: include name
}
