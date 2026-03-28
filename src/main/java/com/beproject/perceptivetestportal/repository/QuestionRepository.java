package com.beproject.perceptivetestportal.repository;

import com.beproject.perceptivetestportal.entity.Question;
import com.beproject.perceptivetestportal.entity.Question.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT * FROM questions q " +
                   "WHERE q.difficulty_level = :difficulty " +
                   "ORDER BY RAND() LIMIT :count",
           nativeQuery = true)
    List<Question> findRandomQuestionsFromPool(@Param("difficulty") String difficulty,
                                               @Param("count") int count);
}