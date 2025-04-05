package com.backendservice.EDU_Connect.repository;



import com.backendservice.EDU_Connect.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findBySemester(Integer semester);
    List<Quiz> findByModuleId(Long moduleId);
}
