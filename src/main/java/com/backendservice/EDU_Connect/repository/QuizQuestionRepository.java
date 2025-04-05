package com.backendservice.EDU_Connect.repository;

import com.backendservice.EDU_Connect.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}
