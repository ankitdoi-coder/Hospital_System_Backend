package com.healthcare.user.controller;

import com.healthcare.user.dto.*;
import com.healthcare.user.service.AuthService;
import com.healthcare.user.service.EmailOtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

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
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
