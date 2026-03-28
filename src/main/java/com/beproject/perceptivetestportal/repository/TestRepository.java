package com.beproject.perceptivetestportal.repository;

import com.beproject.perceptivetestportal.entity.Test;
import com.beproject.perceptivetestportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findByCreatedBy(User user);

    List<Test> findByCreatedByAndIsPublicFalse(User user);

    @Query("SELECT t FROM Test t " +
           "JOIN t.assignedGroups g " +
           "JOIN g.students s " +
           "WHERE s.id = :studentId " +
           "AND t.status = 'PUBLISHED' " +
           "AND (:now BETWEEN t.startTime AND t.endTime)")
    List<Test> findActiveTestsForStudent(@Param("studentId") Long studentId, @Param("now") LocalDateTime now);

    @Query("SELECT t FROM Test t WHERE t.isPublic = true " +
           "AND t.status = 'PUBLISHED' " +
           "AND (:now BETWEEN t.startTime AND t.endTime)")
    List<Test> findActivePublicTests(@Param("now") LocalDateTime now);
}