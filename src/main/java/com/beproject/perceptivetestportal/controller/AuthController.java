package com.beproject.perceptivetestportal.controller;

import com.beproject.perceptivetestportal.dto.LoginRequestDTO;
import com.beproject.perceptivetestportal.dto.LoginResponseDTO;
import com.beproject.perceptivetestportal.entity.User;
import com.beproject.perceptivetestportal.repository.UserRepository;
import com.beproject.perceptivetestportal.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, 
                          JwtUtil jwtUtil, 
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        // Authenticate using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // At this point authentication is successful. 
        // We fetch the user to get ID and Role for the response DTO.
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after successful authentication"));

        // Generate Token
        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDTO(
                "Login successful",
                user.getId(),
                user.getRole(),
                token
        );
    }
}