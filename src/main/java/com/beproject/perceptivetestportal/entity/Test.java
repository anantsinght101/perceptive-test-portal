package com.beproject.perceptivetestportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer durationInMinutes;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder.Default
    @Column(nullable = false)
    private boolean isPublic = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    // ✅ This prevents the "Infinite Loop" when sending the Test as JSON
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password", "createdAt"})
    private User createdBy;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TestStatus status = TestStatus.DRAFT;

    @ManyToMany
    @JoinTable(
        name = "test_questions",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @Builder.Default
    @JsonIgnoreProperties("tests") // ✅ Prevents loop if Question also references Test
    private List<Question> questions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "test_assignments",
        joinColumns = @JoinColumn(name = "test_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    @Builder.Default
    @JsonIgnoreProperties("assignedTests")
    private List<Group> assignedGroups = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public enum TestStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }

    // Helper methods for relationships
    public void addQuestion(Question question) {
        questions.add(question);
        if (question.getTests() != null) {
            question.getTests().add(this);
        }
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        if (question.getTests() != null) {
            question.getTests().remove(this);
        }
    }

    public void assignToGroup(Group group) {
        assignedGroups.add(group);
    }
}