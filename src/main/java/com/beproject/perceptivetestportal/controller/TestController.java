package com.beproject.perceptivetestportal.controller;

import com.beproject.perceptivetestportal.dto.TestRequestDTO;
import com.beproject.perceptivetestportal.entity.Test;
import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.service.TestService;
import com.beproject.perceptivetestportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{testId}/add-question/{questionId}")
@PreAuthorize("hasRole('TEACHER')")
public ResponseEntity<Test> addQuestionToTest(
        @PathVariable Long testId, 
        @PathVariable Long questionId) {
    
    Test updatedTest = testService.addQuestionToTest(testId, questionId);
    return ResponseEntity.ok(updatedTest);
}

    @PostMapping("/tests/create")
    @PreAuthorize("hasAnyRole('TEACHER', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Test> createTest(@RequestBody TestRequestDTO dto, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(testService.createTest(dto, currentUser, false));
    }

    @PostMapping("/tests/personal")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Test> createPersonalTest(@RequestBody TestRequestDTO dto, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(testService.createTest(dto, currentUser, true));
    }

        
}