package com.beproject.perceptivetestportal.service;

import com.beproject.perceptivetestportal.entity.Question;
import com.beproject.perceptivetestportal.entity.Test;
import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.repository.QuestionRepository;
import com.beproject.perceptivetestportal.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Transactional
    public void addQuestionsFromAiPool(Long testId, int count, String difficulty, User user) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        if (!test.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: You do not own this test");
        }

        if (test.getStatus() != Test.TestStatus.DRAFT) {
            throw new RuntimeException("Cannot modify a test that is not in DRAFT status");
        }

        List<Question> randomPool = questionRepository.findRandomQuestionsFromPool(difficulty, count);

        if (randomPool.size() < count) {
            throw new RuntimeException("AI Pool exhausted. Requested: " + count + ", Available: " + randomPool.size());
        }

        // Logic Change: Just add to the collection. JPA handles the Join Table.
        test.getQuestions().addAll(randomPool);
        
        testRepository.save(test);
    }
}