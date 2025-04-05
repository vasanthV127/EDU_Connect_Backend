package com.backendservice.EDU_Connect.payload.request;



import lombok.Data;
import java.util.List;

@Data
public class QuizDTO {
    private Long id;
    private String title;
    private String description;
    private Long moduleId;
    private String moduleName;
    private Long createdById;
    private String createdByName;
    private Integer semester;
    private List<QuizQuestionDTO> questions;
    private boolean isActive;
}

