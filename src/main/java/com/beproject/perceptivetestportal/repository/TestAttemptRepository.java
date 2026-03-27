package com.beproject.perceptivetestportal.repository;

import com.beproject.perceptivetestportal.entity.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {
}