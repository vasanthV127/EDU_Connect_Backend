package com.backendservice.EDU_Connect.payload.request;

import lombok.Data;

import java.util.Map;

@Data
public class QuizSubmissionDTO {
    private Long id;
    private Long quizId;
    private Long submittedById;
    private Map<Long, String> answers;
    private Integer score;
}