package com.beproject.perceptivetestportal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beproject.perceptivetestportal.dto.QuestionRequestDTO;
import com.beproject.perceptivetestportal.entity.Question;
import com.beproject.perceptivetestportal.service.QuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/bulk-add/difficulty")
    public ResponseEntity<String> addByDiff(
            @RequestParam Long testId, 
            @RequestParam String difficulty, 
            @RequestParam int count) {
        // Removed 'user' parameter to prevent NullPointerException
        questionService.bulkAddByDifficulty(testId, difficulty, count);
        return ResponseEntity.ok("Added questions by difficulty.");
    }

    @PostMapping("/bulk-add/subject-difficulty")
    public ResponseEntity<String> addBySubDiff(
            @RequestParam Long testId, 
            @RequestParam String subject, 
            @RequestParam String difficulty, 
            @RequestParam int count) {
        // Removed 'user' parameter to sync with Service logic
        questionService.bulkAddBySubjectAndDifficulty(testId, subject, difficulty, count);
        return ResponseEntity.ok("Added questions by subject and difficulty.");
    }

    @PostMapping("/bulk-add/range")
    public ResponseEntity<String> addByRange(
            @RequestParam Long testId, 
            @RequestParam Long startId, 
            @RequestParam Long endId) {
        // Updated to the new simplified signature
        questionService.bulkAddByIdRange(testId, startId, endId);
        return ResponseEntity.ok("Added questions in range " + startId + " to " + endId);
    }

    @PostMapping("/bulk-save-ai")
    public ResponseEntity<String> bulkSaveAi(
            @RequestBody List<QuestionRequestDTO> dtos,
            @RequestParam String subject) {
        
        questionService.saveAiGeneratedQuestions(dtos, subject);
        return ResponseEntity.ok("Successfully saved " + dtos.size() + " questions to the bank.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/generate-quiz")
public ResponseEntity<List<Question>> generateQuickQuiz(
        @RequestParam String subject,
        @RequestParam String difficulty,
        @RequestParam int questionCount) {
        
    List<Question> questions = questionService.getQuestionsForStudent(subject, difficulty, questionCount);
    return ResponseEntity.ok(questions);
}

}