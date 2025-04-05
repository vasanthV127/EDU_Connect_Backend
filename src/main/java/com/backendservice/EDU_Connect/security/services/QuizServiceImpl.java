package com.backendservice.EDU_Connect.security.services;

import com.backendservice.EDU_Connect.model.*;
import com.backendservice.EDU_Connect.model.Module;
import com.backendservice.EDU_Connect.payload.request.QuizDTO;
import com.backendservice.EDU_Connect.payload.request.QuizQuestionDTO;
import com.backendservice.EDU_Connect.payload.request.QuizSubmissionDTO;
import com.backendservice.EDU_Connect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizSubmissionRepository quizSubmissionRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public QuizDTO createQuiz(Quiz quiz) {
        // Validate and set module
        Module module = moduleRepository.findById(quiz.getModule().getId())
                .orElseThrow(() -> new RuntimeException("Module not found with ID: " + quiz.getModule().getId()));
        quiz.setModule(module);

        // Validate and set createdBy
        User createdBy = userRepository.findById(quiz.getCreatedBy().getId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + quiz.getCreatedBy().getId()));
        quiz.setCreatedBy(createdBy);

        Quiz savedQuiz = quizRepository.save(quiz);
        return mapToQuizDTO(savedQuiz);
    }

    @Override
    public List<QuizDTO> getQuizzesBySemester(Integer semester) {
        return quizRepository.findBySemester(semester).stream()
                .map(this::mapToQuizDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QuizDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + id));
        return mapToQuizDTO(quiz);
    }

    @Override
    public QuizQuestionDTO addQuestionToQuiz(Long quizId, QuizQuestion question) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + quizId));
        question.setQuiz(quiz);
        QuizQuestion savedQuestion = quizQuestionRepository.save(question);
        return mapToQuizQuestionDTO(savedQuestion);
    }

    @Override
    @Transactional
    public List<QuizQuestionDTO> addMultipleQuestionsToQuiz(Long quizId, List<QuizQuestion> questions) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + quizId));
        questions.forEach(question -> question.setQuiz(quiz));
        List<QuizQuestion> savedQuestions = quizQuestionRepository.saveAll(questions);
        return savedQuestions.stream()
                .map(this::mapToQuizQuestionDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuizSubmissionDTO submitQuiz(QuizSubmissionDTO submission) {
        // Check if the user has already submitted this quiz
        Optional<QuizSubmission> existingSubmission = quizSubmissionRepository
                .findByQuizIdAndSubmittedBy_Id(submission.getQuizId(), submission.getSubmittedById());
        if (existingSubmission.isPresent()) {
            throw new RuntimeException("You have already submitted this quiz.");
        }

        // Fetch quiz to calculate score
        Quiz quiz = quizRepository.findById(submission.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + submission.getQuizId()));
        List<QuizQuestion> questions = quiz.getQuestions();
        int score = 0;
        for (QuizQuestion question : questions) {
            String selectedAnswer = submission.getAnswers().get(question.getId());
            if (selectedAnswer != null && selectedAnswer.charAt(0) == question.getCorrectAnswer()) {
                score++;
            }
        }

        // Create and save submission entity
        QuizSubmission entity = new QuizSubmission();
        entity.setQuizId(submission.getQuizId());
        entity.setSubmittedBy(new com.backendservice.EDU_Connect.model.User());
        entity.getSubmittedBy().setId(submission.getSubmittedById());
        entity.setAnswers(submission.getAnswers());
        entity.setScore(score);

        QuizSubmission savedEntity = quizSubmissionRepository.save(entity);
        return mapToQuizSubmissionDTO(savedEntity);
    }

    @Override
    public List<QuizDTO> getQuizzesByModuleId(Long moduleId) {
        return quizRepository.findByModuleId(moduleId).stream()
                .map(this::mapToQuizDTO)
                .collect(Collectors.toList());
    }

    // New method to check if a user has submitted a quiz
    public QuizSubmissionDTO getSubmissionByQuizAndUser(Long quizId, Long userId) {
        Optional<QuizSubmission> submission = quizSubmissionRepository
                .findByQuizIdAndSubmittedBy_Id(quizId, userId);
        return submission.map(this::mapToQuizSubmissionDTO).orElse(null);
    }

    // Helper methods to map entities to DTOs
    private QuizDTO mapToQuizDTO(Quiz quiz) {
        QuizDTO dto = new QuizDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setModuleId(quiz.getModule().getId());
        dto.setModuleName(quiz.getModule().getName());
        dto.setCreatedById(quiz.getCreatedBy().getId());
        dto.setCreatedByName(quiz.getCreatedBy().getName());
        dto.setSemester(quiz.getSemester());
        dto.setQuestions(quiz.getQuestions().stream()
                .map(this::mapToQuizQuestionDTO)
                .collect(Collectors.toList()));
        dto.setActive(quiz.isActive());
        return dto;
    }

    private QuizQuestionDTO mapToQuizQuestionDTO(QuizQuestion question) {
        QuizQuestionDTO dto = new QuizQuestionDTO();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        return dto;
    }

    private QuizSubmissionDTO mapToQuizSubmissionDTO(QuizSubmission submission) {
        QuizSubmissionDTO dto = new QuizSubmissionDTO();
        dto.setId(submission.getId());
        dto.setQuizId(submission.getQuizId());
        dto.setSubmittedById(submission.getSubmittedBy().getId());
        dto.setAnswers(submission.getAnswers());
        dto.setScore(submission.getScore());
        return dto;
    }
}