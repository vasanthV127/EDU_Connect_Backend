package com.backendservice.EDU_Connect.repository;

import com.backendservice.EDU_Connect.model.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    Optional<QuizSubmission> findByQuizIdAndSubmittedBy_Id(Long quizId, Long userId);
}
