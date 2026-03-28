package com.beproject.perceptivetestportal.controller;

import com.beproject.perceptivetestportal.dto.LoginRequestDTO;
import com.beproject.perceptivetestportal.dto.LoginResponseDTO;
import com.beproject.perceptivetestportal.dto.UserRequestDTO;
import com.beproject.perceptivetestportal.dto.UserResponseDTO;
import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.repository.UserRepository;
import com.beproject.perceptivetestportal.security.JwtUtil;
import com.beproject.perceptivetestportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    
    // 👇 This is where the UserService goes
    private final UserService userService;

    // 👇 This is your new Register API endpoint
    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserRequestDTO request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication"));

        String token = jwtUtil.generateToken(user.getEmail());

        return LoginResponseDTO.builder()
                .message("Login successful")
                .userId(user.getId())
                .role(user.getRole())
                .token(token)
                .build();
    }
}