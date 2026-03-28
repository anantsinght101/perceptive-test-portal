package com.beproject.perceptivetestportal.controller;
import jakarta.validation.Valid;
import com.beproject.perceptivetestportal.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.beproject.perceptivetestportal.dto.UserRequestDTO;
import com.beproject.perceptivetestportal.dto.UserResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO user) {
        return userService.createUser(user);
    }

    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}