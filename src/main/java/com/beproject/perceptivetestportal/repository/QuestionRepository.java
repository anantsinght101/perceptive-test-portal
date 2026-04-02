package com.beproject.perceptivetestportal.repository;

import com.beproject.perceptivetestportal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // ✅ Finds random questions matching BOTH subject and difficulty
    @Query(value = "SELECT * FROM questions q " +
                   "WHERE LOWER(TRIM(q.subject)) = LOWER(TRIM(:subject)) AND LOWER(TRIM(q.difficulty)) = LOWER(TRIM(:difficulty)) " + 
                   "ORDER BY RAND() LIMIT :count",
            nativeQuery = true)
    List<Question> findRandomBySubjectIgnoreCaseAndDifficultyIgnoreCase(@Param("subject") String subject, 
                                                    @Param("difficulty") String difficulty,
                                                    @Param("count") int count);
    @Query(value = "SELECT * FROM questions q WHERE LOWER(TRIM(q.difficulty)) = LOWER(TRIM(:difficulty)) ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> findRandomByDifficultyIgnorecase(@Param("difficulty") String difficulty, @Param("count") int count);

    @Query(value = "SELECT * FROM questions q WHERE q.id BETWEEN :startId AND :endId", nativeQuery = true)
    List<Question> findByIdRange(@Param("startId") Long startId, @Param("endId") Long endId);

@Query(value = "SELECT * FROM questions WHERE subject = :subject AND difficulty = :difficulty ORDER BY RAND() LIMIT :count", nativeQuery = true)
List<Question> findRandomQuestions(@Param("subject") String subject, @Param("difficulty") String difficulty, @Param("count") int count);


}