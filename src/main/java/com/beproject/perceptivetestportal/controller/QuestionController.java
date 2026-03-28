package com.beproject.perceptivetestportal.controller;

import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*; // ✅ This import fixes all 15 errors

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/bulk-add/difficulty")
    public ResponseEntity<String> addByDiff(
            @RequestParam Long testId, 
            @RequestParam String difficulty, 
            @RequestParam int count, 
            @AuthenticationPrincipal User user) {
        questionService.bulkAddByDifficulty(testId, difficulty, count, user);
        return ResponseEntity.ok("Added questions by difficulty.");
    }

    @PostMapping("/bulk-add/subject-difficulty")
    public ResponseEntity<String> addBySubDiff(
            @RequestParam Long testId, 
            @RequestParam String subject, 
            @RequestParam String difficulty, 
            @RequestParam int count, 
            @AuthenticationPrincipal User user) {
        questionService.bulkAddBySubjectAndDifficulty(testId, subject, difficulty, count, user);
        return ResponseEntity.ok("Added questions by subject and difficulty.");
    }

    @PostMapping("/bulk-add/range")
    public ResponseEntity<String> addByRange(
            @RequestParam Long testId, 
            @RequestParam Long startId, 
            @RequestParam Long endId, 
            @AuthenticationPrincipal User user) {
        questionService.bulkAddByIdRange(testId, startId, endId, user);
        return ResponseEntity.ok("Added questions in range " + startId + " to " + endId);
    }
}