package com.beproject.perceptivetestportal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer durationInMinutes;

    @Column(nullable = false)
    private boolean isPublic = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User createdBy;

    @Enumerated(EnumType.STRING)
    private TestStatus status = TestStatus.DRAFT;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Default Constructor (Required by JPA)
    public Test() {}

    // Parameterized Constructor
    public Test(String title, String description, Integer durationInMinutes, User createdBy) {
        this.title = title;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.createdBy = createdBy;
        this.status = TestStatus.DRAFT;
    }

    // --- Relationship Helper Methods ---
    public void addQuestion(Question question) {
        questions.add(question);
        question.setTest(this);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setTest(null);
    }
public enum TestStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }

    @Enumerated(EnumType.STRING)
    
    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDurationInMinutes() { return durationInMinutes; }
    public void setDurationInMinutes(Integer durationInMinutes) { this.durationInMinutes = durationInMinutes; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public TestStatus getStatus() { return status; }
    public void setStatus(TestStatus status) { this.status = status; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}