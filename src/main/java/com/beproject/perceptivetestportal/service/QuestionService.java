package com.beproject.perceptivetestportal.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beproject.perceptivetestportal.dto.QuestionRequestDTO;
import com.beproject.perceptivetestportal.entity.Question;
import com.beproject.perceptivetestportal.entity.Test;
import com.beproject.perceptivetestportal.repository.QuestionRepository;
import com.beproject.perceptivetestportal.repository.TestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    // Helper: Simplified for Demo
    private Test getValidatedTest(Long testId) {
        return testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found with ID: " + testId));
    }

    @Transactional
    public void bulkAddByDifficulty(Long testId, String difficulty, int count) {
        Test test = getValidatedTest(testId);
        String cleanDiff = difficulty.split(" ")[0].toUpperCase().trim();
        
        List<Question> questions = questionRepository.findRandomByDifficultyIgnorecase(cleanDiff, count);
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for Difficulty: " + cleanDiff);
        }
        
        questions.forEach(test::addQuestion);
        testRepository.save(test);
    }

    @Transactional
    public void bulkAddBySubjectAndDifficulty(Long testId, String subject, String difficulty, int count) {
        Test test = getValidatedTest(testId);
        String cleanDiff = difficulty.split(" ")[0].toUpperCase().trim();
        String cleanSub = subject.trim();

        List<Question> questions = questionRepository.findRandomBySubjectIgnoreCaseAndDifficultyIgnoreCase(cleanSub, cleanDiff, count);
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for Subject: " + cleanSub + " and Difficulty: " + cleanDiff);
        }
        
        questions.forEach(test::addQuestion);
        testRepository.save(test);
    }

    @Transactional
    public void bulkAddByIdRange(Long testId, Long startId, Long endId) {
        Test test = getValidatedTest(testId); // Removed 'user' parameter
        List<Question> questions = questionRepository.findByIdRange(startId, endId);
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found in ID range: " + startId + " to " + endId);
        }
        questions.forEach(test::addQuestion);
        testRepository.save(test);
    }

    @Transactional
    public Question createQuestion(QuestionRequestDTO dto) {
        Question question = Question.builder()
                .content(dto.getContent())
                .subject(dto.getSubject())
                .difficulty(Question.Difficulty.valueOf(dto.getDifficulty().toUpperCase()))
                .type(Question.QuestionType.valueOf(dto.getType().toUpperCase()))
                .options(dto.getOptions())
                .correctAnswer(dto.getCorrectAnswer())
                .build();

        return questionRepository.save(question);
    }

    @Transactional
    public void saveAiGeneratedQuestions(List<QuestionRequestDTO> dtos, String subject) {
        List<Question> questions = dtos.stream().map(dto -> 
            Question.builder()
                    .content(dto.getContent())
                    .subject(subject)
                    .difficulty(Question.Difficulty.MEDIUM)
                    .type(Question.QuestionType.MCQ)
                    .options(dto.getOptions())
                    .correctAnswer(dto.getCorrectAnswer())
                    .build()
        ).toList();

        questionRepository.saveAll(questions);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    public List<Question> getQuestionsForStudent(String subject, String difficulty, int count) {
    List<Question> questions = questionRepository.findRandomQuestions(subject, difficulty, count);
    
    // Safety check: Remove answers so students can't cheat via Inspect Element
    questions.forEach(q -> q.setCorrectAnswer(null)); 
    
    return questions;
}
}