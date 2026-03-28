package com.beproject.perceptivetestportal.service;

import com.beproject.perceptivetestportal.dto.UserRequestDTO;
import com.beproject.perceptivetestportal.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO user);

    List<UserResponseDTO> getAllUsers();
}