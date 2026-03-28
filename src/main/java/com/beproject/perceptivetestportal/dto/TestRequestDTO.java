package com.beproject.perceptivetestportal.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRequestDTO {
    private String title;
    private String description;
    private Integer durationInMinutes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isPublic;
}