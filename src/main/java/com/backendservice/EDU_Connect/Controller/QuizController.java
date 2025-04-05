package com.backendservice.EDU_Connect.Controller;

import com.backendservice.EDU_Connect.model.Quiz;
import com.backendservice.EDU_Connect.model.QuizQuestion;
import com.backendservice.EDU_Connect.payload.request.QuizDTO;
import com.backendservice.EDU_Connect.payload.request.QuizQuestionDTO;
import com.backendservice.EDU_Connect.payload.request.QuizSubmissionDTO;
import com.backendservice.EDU_Connect.security.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<QuizDTO> createQuiz(@RequestBody Quiz quiz) {
        QuizDTO createdQuiz = quizService.createQuiz(quiz);
        return ResponseEntity.ok(createdQuiz);
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<QuizDTO>> getQuizzesBySemester(@PathVariable Integer semester) {
        List<QuizDTO> quizzes = quizService.getQuizzesBySemester(semester);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizDetails(@PathVariable Long id) {
        QuizDTO quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<QuizQuestionDTO> assignQuestion(
            @PathVariable Long quizId,
            @RequestBody QuizQuestion question) {
        QuizQuestionDTO createdQuestion = quizService.addQuestionToQuiz(quizId, question);
        return ResponseEntity.ok(createdQuestion);
    }

    @PostMapping("/{quizId}/questions/batch")
    public ResponseEntity<List<QuizQuestionDTO>> assignMultipleQuestions(
            @PathVariable Long quizId,
            @RequestBody List<QuizQuestion> questions) {
        List<QuizQuestionDTO> createdQuestions = quizService.addMultipleQuestionsToQuiz(quizId, questions);
        return ResponseEntity.ok(createdQuestions);
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<?> submitQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizSubmissionDTO submission) {
        try {
            submission.setQuizId(quizId);
            QuizSubmissionDTO savedSubmission = quizService.submitQuiz(submission);
            return ResponseEntity.ok(savedSubmission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{quizId}/submit")
    public ResponseEntity<QuizSubmissionDTO> getSubmission(
            @PathVariable Long quizId,
            @RequestParam Long userId) {
        QuizSubmissionDTO submission = quizService.getSubmissionByQuizAndUser(quizId, userId);
        if (submission != null) {
            return ResponseEntity.ok(submission);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<QuizDTO>> getQuizzesByModuleId(@PathVariable Long moduleId) {
        List<QuizDTO> quizzes = quizService.getQuizzesByModuleId(moduleId);
        return ResponseEntity.ok(quizzes);
    }
}