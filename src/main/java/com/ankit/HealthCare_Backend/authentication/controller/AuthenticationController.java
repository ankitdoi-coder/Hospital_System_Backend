package com.ankit.HealthCare_Backend.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;

import com.ankit.HealthCare_Backend.shared.dto.MessageResponseDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import com.ankit.HealthCare_Backend.usermanagement.doctor.repository.DoctorRepository;
import com.ankit.HealthCare_Backend.usermanagement.user.service.UserDetailsService;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;
import com.ankit.HealthCare_Backend.authentication.security.JwtService;
import com.ankit.HealthCare_Backend.authentication.service.AuthService;
import com.ankit.HealthCare_Backend.authentication.dto.*;
import com.ankit.HealthCare_Backend.authentication.dto.LoginRequestDTO;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class AuthenticationController {
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;
    private final UserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    
    @Autowired
    AuthService authService;

    @Autowired
    private UserRepository userRepository;



    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("User with this email already exists");
        }
        UserResponseDTO response = authService.registerUser(registerRequest);
        if(response!=null){
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }else{
            throw new RuntimeException("Internal Server Error");
        }
       
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );
            
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());

            boolean isDoctor = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"));

            if (isDoctor) {
                User user = userRepo.findByEmail(userDetails.getUsername());
                Doctor doctor = doctorRepo.findByUserId(user.getId());

                if (doctor != null && doctor.isApproved()) {
                    final String jwt = jwtService.generateToken(userDetails);
                    return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login successful"));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new LoginResponseDTO(null, "Your doctor account is not approved yet. Contact admin or wait for approval.")
                    );
                }
            } else {
                final String jwt = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login successful"));
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new LoginResponseDTO(null, "Invalid email or password")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new LoginResponseDTO(null, "An error occurred during login: " + e.getMessage())
            );
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDTO> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        try {
            String message = authService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(new MessageResponseDTO(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        try {
            String message = authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponseDTO(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
        }
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