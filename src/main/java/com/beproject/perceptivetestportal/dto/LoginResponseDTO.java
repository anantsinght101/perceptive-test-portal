package com.beproject.perceptivetestportal.dto;

import com.beproject.perceptivetestportal.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
    private String message;
    private Long userId;
    private Role role;
    private String token;
}