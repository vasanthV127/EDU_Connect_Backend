package com.backendservice.EDU_Connect.payload.request;

import lombok.Data;


@Data
public class QuizQuestionDTO {
    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private char correctAnswer;
}