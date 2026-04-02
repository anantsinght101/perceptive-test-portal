package com.beproject.perceptivetestportal.service;

import com.beproject.perceptivetestportal.dto.TestRequestDTO;
import com.beproject.perceptivetestportal.entity.Group;
import com.beproject.perceptivetestportal.entity.Question;
import com.beproject.perceptivetestportal.entity.Test;
import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.repository.GroupRepository;
import com.beproject.perceptivetestportal.repository.QuestionRepository;
import com.beproject.perceptivetestportal.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor // ✅ Replaces @Autowired for better constructor injection
public class TestService {

    private final TestRepository testRepository;
    private final GroupRepository groupRepository;
    private final QuestionRepository questionRepository; // ✅ Added missing repository

    public Test createTest(TestRequestDTO dto, User currentUser, boolean forcePrivate) {
        // Handle boolean naming: check if DTO uses isPublic() or getIsPublic()
        boolean publicStatus = forcePrivate ? false : dto.isPublic(); // ✅ Matches Lombok convention
        
        // ✅ Using the Test.builder() with initialized empty lists
        return testRepository.save(Test.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .durationInMinutes(dto.getDurationInMinutes())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .createdBy(currentUser)
                .isPublic(publicStatus)
                .status(Test.TestStatus.DRAFT)
                .questions(new ArrayList<>())
                .assignedGroups(new ArrayList<>())
                .build());
    }

    @Transactional
    public Test addQuestionToTest(Long testId, Long questionId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + testId));
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        test.addQuestion(question);
        return testRepository.save(test);
    }

    @Transactional
    public void assignTestToGroup(Long testId, Long groupId, User currentUser) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // ✅ Lombok @Getter provides getCreatedBy() and getId()
        if (!test.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized: You do not own this test");
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (!test.getAssignedGroups().contains(group)) {
            test.assignToGroup(group);
            testRepository.save(test);
        }
    }

    public List<Test> getTestsByUser(User currentUser) {
        return testRepository.findByCreatedBy(currentUser);
    }
}