package com.healthcare.user.service;

import com.healthcare.user.dto.AuthResponseDTO;
import com.healthcare.user.dto.LoginRequestDTO;
import com.healthcare.user.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}
