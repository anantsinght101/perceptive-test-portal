package com.beproject.perceptivetestportal.service;

import com.beproject.perceptivetestportal.entity.Question;
import com.beproject.perceptivetestportal.entity.Test;
import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.repository.QuestionRepository;
import com.beproject.perceptivetestportal.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beproject.perceptivetestportal.dto.QuestionRequestDTO;
import java.util.List;

@Service
@RequiredArgsConstructor // ✅ Better than @Autowired for constructor injection
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;

    private Test getValidatedTest(Long testId, User user) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        if (!test.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: Access Denied");
        }

        return test;
    }

    @Transactional
    public void bulkAddByDifficulty(Long testId, String difficulty, int count, User user) {
        Test test = getValidatedTest(testId, user);
        List<Question> questions = questionRepository.findRandomByDifficulty(difficulty, count);
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for Difficulty: " + difficulty);
        }
        questions.forEach(test::addQuestion);
        testRepository.save(test);
    }

    @Transactional
    public void bulkAddBySubjectAndDifficulty(Long testId, String subject, String difficulty, int count, User user) {
        Test test = getValidatedTest(testId, user);
        List<Question> questions = questionRepository.findRandomBySubjectAndDifficulty(subject, difficulty, count);
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for Subject: " + subject + " and Difficulty: " + difficulty);
        }
        questions.forEach(test::addQuestion);
        testRepository.save(test);
    }

    @Transactional
    public void bulkAddByIdRange(Long testId, Long startId, Long endId, User user) {
        Test test = getValidatedTest(testId, user);
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
            .options(dto.getOptions()) // ✅ Correctly maps the List to the ElementCollection
            .correctAnswer(dto.getCorrectAnswer())
            .build();

    return questionRepository.save(question);
}



}