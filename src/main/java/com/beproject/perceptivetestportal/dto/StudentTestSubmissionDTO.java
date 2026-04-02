package com.beproject.perceptivetestportal.dto;

import lombok.Data;
import java.util.Map;

@Data
public class StudentTestSubmissionDTO {
    private String subject;
    private String difficulty;
    
    // This will hold the answers. Key = Question ID, Value = Selected Option ("A", "B", "C", "D")
    private Map<Long, String> answers; 
}