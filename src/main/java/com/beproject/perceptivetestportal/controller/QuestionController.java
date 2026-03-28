package com.beproject.perceptivetestportal.controller;

import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.repository.UserRepository;
import com.beproject.perceptivetestportal.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add-auto")
    @PreAuthorize("hasAnyRole('TEACHER', 'DEPARTMENT_ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> addAutoQuestions(@RequestBody Map<String, Object> request, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long testId = Long.valueOf(request.get("testId").toString());
        int count = Integer.parseInt(request.get("numberOfQuestions").toString());
        String difficulty = request.get("difficulty").toString().toUpperCase();

        questionService.addQuestionsFromAiPool(testId, count, difficulty, currentUser);

        return ResponseEntity.ok(Map.of("message", count + " questions added successfully from the AI pool"));
    }
}