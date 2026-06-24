package com.ankit.HealthCare_Backend.authentication.service;

import com.ankit.HealthCare_Backend.authentication.dto.LoginRequestDTO;
import com.ankit.HealthCare_Backend.authentication.dto.LoginResponseDTO;
import com.ankit.HealthCare_Backend.authentication.dto.UserResponseDTO;
import com.ankit.HealthCare_Backend.authentication.dto.RegisterRequestDTO;

public interface AuthService {
    UserResponseDTO registerUser(RegisterRequestDTO registerRequest);
    LoginResponseDTO login(LoginRequestDTO loginRequest);
    String forgotPassword(String email);
    String resetPassword(String token, String newPassword);
}
