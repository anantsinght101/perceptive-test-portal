package com.beproject.perceptivetestportal.repository;

import com.beproject.perceptivetestportal.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}