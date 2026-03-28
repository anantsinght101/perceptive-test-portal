package com.beproject.perceptivetestportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private String category; // e.g., "Java", "Python", "Math"

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @Id)
    @Column(name = "option_text")
    @Builder.Default
    private List<String> options = new ArrayList<>();

    private String correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @ManyToMany(mappedBy = "questions")
    @Builder.Default
    // ✅ Prevents Question -> Test -> Question loop
    @JsonIgnoreProperties("questions") 
    private List<Test> tests = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum QuestionType {
        MCQ, TRUE_FALSE, SHORT_ANSWER
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}