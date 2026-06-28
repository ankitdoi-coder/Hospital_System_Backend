package com.ankit.HealthCare_Backend.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;

import lombok.RequiredArgsConstructor;

import com.ankit.HealthCare_Backend.authentication.service.EmailOtpService;
import com.ankit.HealthCare_Backend.shared.dto.MessageResponseDTO;
import com.ankit.HealthCare_Backend.authentication.service.AuthService;
import com.ankit.HealthCare_Backend.authentication.dto.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    private final EmailOtpService emailOtpService;


    @PostMapping("/send-otp")
    public ResponseEntity<MessageResponseDTO> sendOtp(@Valid @RequestBody OtpRequestDTO request) {
        emailOtpService.sendOtp(request.getEmail());
        return ResponseEntity.ok(new MessageResponseDTO("OTP sent to " + request.getEmail()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<MessageResponseDTO> verifyOtp(@Valid @RequestBody OtpVerifyDTO request) {
        boolean verified = emailOtpService.verifyOtp(request.getEmail(), request.getOtp());
        if (!verified) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid or expired OTP"));
        }
        return ResponseEntity.ok(new MessageResponseDTO("Email verified. You can now register."));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        UserResponseDTO response = authService.registerUser(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        String message = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(new MessageResponseDTO(message));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        String message = authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponseDTO(message));
    }

    @GetMapping("/oauth2/callback")
    public void oauthCallback(@RequestParam String token, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:5173/login?token=" + token);
    }

    @GetMapping("/oauth2/error")
    public void oauthError(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:5173/login?error=OAUTH_FAILED");
    }
}