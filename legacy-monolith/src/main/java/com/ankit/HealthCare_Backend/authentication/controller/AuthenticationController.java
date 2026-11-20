package com.ankit.HealthCare_Backend.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ankit.HealthCare_Backend.authentication.service.EmailOtpService;
import com.ankit.HealthCare_Backend.shared.dto.MessageResponseDTO;
import com.ankit.HealthCare_Backend.authentication.service.AuthService;
import com.ankit.HealthCare_Backend.authentication.dto.*;
import com.ankit.HealthCare_Backend.authentication.security.JwtService;

@Tag(name = "Authentication", description = "Register, Login, OTP verification, Password reset and OAuth2 endpoints")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;
    private final EmailOtpService emailOtpService;
    private final JwtService jwtService;

    @Operation(summary = "Send OTP to email", description = "Sends a 6-digit OTP to the given email before registration")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing email")
    })
    @PostMapping("/send-otp")
    public ResponseEntity<MessageResponseDTO> sendOtp(@Valid @RequestBody OtpRequestDTO request) {
        emailOtpService.sendOtp(request.getEmail());
        return ResponseEntity.ok(new MessageResponseDTO("OTP sent to " + request.getEmail()));
    }

    @Operation(summary = "Verify OTP", description = "Verifies the OTP entered by the user. OTP expires after configured minutes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email verified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired OTP")
    })
    @PostMapping("/verify-otp")
    public ResponseEntity<MessageResponseDTO> verifyOtp(@Valid @RequestBody OtpVerifyDTO request) {
        boolean verified = emailOtpService.verifyOtp(request.getEmail(), request.getOtp());
        if (!verified) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Invalid or expired OTP"));
        }
        return ResponseEntity.ok(new MessageResponseDTO("Email verified. You can now register."));
    }

    @Operation(summary = "Register a new user", description = "Registers a new Patient or Doctor. roleId: 2 = Patient, 3 = Doctor. Doctor accounts require admin approval before login")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        UserResponseDTO response = authService.registerUser(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Login", description = "Authenticates user with email and password. Returns a JWT token to use in the Authorize button above")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "Doctor account not yet approved by admin")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "Forgot password", description = "Sends a password reset link to the registered email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reset link sent to email"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        String message = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(new MessageResponseDTO(message));
    }

    @Operation(summary = "Reset password", description = "Resets the password using the token received in the reset email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired reset token")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        String message = authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponseDTO(message));
    }

    @Operation(summary = "Verify reset token", description = "Checks if the password reset token is still valid before showing the reset form")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns true if token is valid, false if expired")
    })
    @PostMapping("/verify-reset-token")
    public ResponseEntity<Boolean> verifyResetToken(@Valid @RequestBody VerifyResetTokenRequestDTO request) {
        boolean isValid = authService.verifyResetToken(request.getEmail(), request.getToken());
        return ResponseEntity.ok(isValid);
    }

    @Operation(summary = "OAuth2 callback", description = "Redirects to frontend with JWT token after successful Google login")
    @ApiResponse(responseCode = "302", description = "Redirects to frontend login page with token")
    @GetMapping("/oauth2/callback")
    public void oauthCallback(@RequestParam String token, HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:5173/login?token=" + token);
    }

    @Operation(summary = "OAuth2 error", description = "Redirects to frontend with error message if Google login fails")
    @ApiResponse(responseCode = "302", description = "Redirects to frontend login page with error")
    @GetMapping("/oauth2/error")
    public void oauthError(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:5173/login?error=OAUTH_FAILED");
    }

    @Operation(summary = " JWT logout via blacklist")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        jwtService.blacklistToken(token);
        return ResponseEntity.ok().build();
    }
}
