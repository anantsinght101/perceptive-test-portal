package com.beproject.perceptivetestportal.dto;
import lombok.Data;
import java.util.List;


@Data
public class QuestionRequestDTO {
    private String content;
    private String subject;
    private String difficulty;
    private String type;
    private List<String> options; // ["Option A", "Option B", "Option C"]
    private String correctAnswer;
}