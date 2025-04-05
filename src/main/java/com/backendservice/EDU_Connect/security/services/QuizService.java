package com.backendservice.EDU_Connect.security.services;
import com.backendservice.EDU_Connect.model.Quiz;
import com.backendservice.EDU_Connect.model.QuizQuestion;
import com.backendservice.EDU_Connect.model.QuizSubmission;
import com.backendservice.EDU_Connect.payload.request.QuizDTO;
import com.backendservice.EDU_Connect.payload.request.QuizQuestionDTO;
import com.backendservice.EDU_Connect.payload.request.QuizSubmissionDTO;

import java.util.List;



public interface QuizService {
    QuizDTO createQuiz(Quiz quiz); // Returns DTO instead of entity
    List<QuizDTO> getQuizzesBySemester(Integer semester);
    QuizDTO getQuizById(Long id);
    QuizQuestionDTO addQuestionToQuiz(Long quizId, QuizQuestion question);
    List<QuizQuestionDTO> addMultipleQuestionsToQuiz(Long quizId, List<QuizQuestion> questions);
    QuizSubmissionDTO submitQuiz(QuizSubmissionDTO submission); // Updated to use DTO
    List<QuizDTO> getQuizzesByModuleId(Long moduleId);
    QuizSubmissionDTO getSubmissionByQuizAndUser(Long quizId, Long userId);
}
