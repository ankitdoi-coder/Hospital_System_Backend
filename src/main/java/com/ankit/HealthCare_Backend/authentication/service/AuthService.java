package com.ankit.HealthCare_Backend.authentication.service;

import com.ankit.HealthCare_Backend.authentication.dto.UserResponseDTO;
import com.ankit.HealthCare_Backend.authentication.dto.RegisterRequestDTO;

public interface AuthService {
    //to register the user
    UserResponseDTO registerUser(RegisterRequestDTO registerRequest);
    
    //for forgot password
    String forgotPassword(String email);
    
    //for reset password
    String resetPassword(String token, String newPassword);
}
