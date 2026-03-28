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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Standardized prefix
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserRequestDTO request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        // Perform standard authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Fetch user from DB to get ID and Role (now visible thanks to Lombok @Getter)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        // ✅ This will now compile because we added @Builder to LoginResponseDTO
        return LoginResponseDTO.builder()
                .message("Login successful")
                .userId(user.getId())
                .role(user.getRole())
                .token(token)
                .build();
    }
}