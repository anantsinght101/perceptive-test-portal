package com.beproject.perceptivetestportal.service;

import com.beproject.perceptivetestportal.dto.TestRequestDTO;
import com.beproject.perceptivetestportal.entity.Group;
import com.beproject.perceptivetestportal.entity.Test;
import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.repository.GroupRepository;
import com.beproject.perceptivetestportal.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private GroupRepository groupRepository;

    public Test createTest(TestRequestDTO dto, User currentUser, boolean forcePrivate) {
        boolean publicStatus = forcePrivate ? false : dto.isPublic();
        
        Test test = Test.builder()
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
                .build();

        return testRepository.save(test);
    }

    @Transactional
    public void assignTestToGroup(Long testId, Long groupId, User currentUser) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

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
}