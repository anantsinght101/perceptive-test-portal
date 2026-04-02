package com.beproject.perceptivetestportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.beproject.perceptivetestportal.service.GenerateQuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Slf4j
public class GenerateQuestionController {

    private final GenerateQuestionService generateQuestionService;

    /**
     * Endpoint to generate MCQs from an uploaded PDF using the AI Pipeline.
     * Accessible via: POST /api/ai/generate
     */
    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> generateQuestions(
            @RequestParam("pdf") MultipartFile file,
            @RequestParam("subject") String subject,
            @RequestParam(value = "num", defaultValue = "20") int num,
            @RequestParam(value = "mode", defaultValue = "easy") String mode) {

        log.info("Received AI generation request for subject: {} with mode: {}", subject, mode);

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a valid PDF file.");
        }

        try {
            // Trigger the service logic
            String jsonResult = generateQuestionService.generateFromPdf(file, subject, num, mode);
            
            // Return the generated JSON MCQs to the frontend
            return ResponseEntity.ok(jsonResult);

        } catch (Exception e) {
            log.error("Error during AI question generation: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("AI Generation Failed: " + e.getMessage());
        }
    }
}