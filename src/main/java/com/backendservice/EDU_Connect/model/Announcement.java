package com.backendservice.EDU_Connect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String datePosted; // You could use LocalDateTime instead for better date handling

    // Many announcements belong to one module
    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    @JsonBackReference
    private Module module;

    // Optional: Reference to the user who posted the announcement
    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;
}