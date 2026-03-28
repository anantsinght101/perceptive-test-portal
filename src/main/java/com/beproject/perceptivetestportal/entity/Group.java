    package com.beproject.perceptivetestportal.entity;
    import jakarta.persistence.*;
    import lombok.*;
    import java.util.ArrayList;
    import java.util.List;


    @Entity
    @Table(name = "student_groups")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public class Group {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String groupName;

        @ManyToMany
        @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            
            inverseJoinColumns = @JoinColumn(name = "student_id")
        )
        
        @Builder.Default
        private List<User> students = new ArrayList<>();
    }