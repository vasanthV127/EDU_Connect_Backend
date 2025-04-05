package com.backendservice.EDU_Connect.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quiz_submissions")
public class QuizSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_id", nullable = false)
    private Long quizId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User submittedBy;

    // Map of question ID to selected answer (e.g., "A", "B", "C", "D")
    @ElementCollection
    @MapKeyColumn(name = "question_id")
    @Column(name = "selected_answer")
    private Map<Long, String> answers;

    private Integer score; // Calculated score after submission
}