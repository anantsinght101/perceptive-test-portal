package com.beproject.perceptivetestportal.repository;

import com.beproject.perceptivetestportal.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // ✅ Finds random questions matching BOTH subject and difficulty
    @Query(value = "SELECT * FROM questions q " +
                   "WHERE q.subject = :subject AND q.difficulty = :difficulty " + 
                   "ORDER BY RAND() LIMIT :count",
            nativeQuery = true)
    List<Question> findRandomBySubjectAndDifficulty(@Param("subject") String subject, 
                                                    @Param("difficulty") String difficulty,
                                                    @Param("count") int count);
    @Query(value = "SELECT * FROM questions q WHERE q.difficulty = :difficulty ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> findRandomByDifficulty(@Param("difficulty") String difficulty, @Param("count") int count);

    @Query(value = "SELECT * FROM questions q WHERE q.id BETWEEN :startId AND :endId", nativeQuery = true)
    List<Question> findByIdRange(@Param("startId") Long startId, @Param("endId") Long endId);
}