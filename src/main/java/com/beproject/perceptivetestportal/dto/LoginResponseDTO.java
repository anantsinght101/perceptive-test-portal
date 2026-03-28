package com.beproject.perceptivetestportal.dto;

import com.beproject.perceptivetestportal.entity.Role;

public class LoginResponseDTO {

    private String message;
    private Long userId;
    private Role role;
    private String token;
    public LoginResponseDTO(String message, Long userId, Role role, String token) {
    this.message = message;
    this.userId = userId;
    this.role = role;
    this.token = token;
}
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    



    // getters & setters
}