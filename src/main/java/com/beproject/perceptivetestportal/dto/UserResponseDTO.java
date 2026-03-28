package com.beproject.perceptivetestportal.dto;

import com.beproject.perceptivetestportal.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ This enables the .builder() method used in UserServiceImpl
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;
}