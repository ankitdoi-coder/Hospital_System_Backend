package com.healthcare.user.service;

import com.healthcare.user.dto.AuthResponseDTO;
import com.healthcare.user.dto.LoginRequestDTO;
import com.healthcare.user.dto.RegisterRequestDTO;
import com.healthcare.user.entity.Doctor;
import com.healthcare.user.entity.Patient;
import com.healthcare.user.entity.User;
import com.healthcare.user.enums.Role;
import com.healthcare.user.repository.DoctorRepository;
import com.healthcare.user.repository.PatientRepository;
import com.healthcare.user.repository.UserRepository;
import com.healthcare.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        Role role = request.getRole() != null ? request.getRole() : Role.ROLE_PATIENT;

        // 1. Create base User
        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(role)
                .isEnabled(true)
                .build();

        User savedUser = userRepository.save(user);
        Long profileId = null;

        // 2. Create specific Role profile
        if (role == Role.ROLE_PATIENT) {
            Patient patient = Patient.builder()
                    .user(savedUser)
                    .bloodGroup(request.getBloodGroup())
                    .gender(request.getGender())
                    .build();
            Patient savedPatient = patientRepository.save(patient);
            profileId = savedPatient.getId();
        } else if (role == Role.ROLE_DOCTOR) {
            Doctor doctor = Doctor.builder()
                    .user(savedUser)
                    .specialization(request.getSpecialization() != null ? request.getSpecialization() : "General Physician")
                    .department(request.getDepartment() != null ? request.getDepartment() : "General Medicine")
                    .consultationFee(request.getConsultationFee() != null ? request.getConsultationFee() : 500.0)
                    .isApproved(true)
                    .build();
            Doctor savedDoctor = doctorRepository.save(doctor);
            profileId = savedDoctor.getId();
        }

        // 3. Generate JWT Token
        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getRole().name(), savedUser.getId());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole())
                .profileId(profileId)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        Long profileId = null;
        if (user.getRole() == Role.ROLE_PATIENT) {
            profileId = patientRepository.findByUserId(user.getId())
                    .map(Patient::getId)
                    .orElse(null);
        } else if (user.getRole() == Role.ROLE_DOCTOR) {
            profileId = doctorRepository.findByUserId(user.getId())
                    .map(Doctor::getId)
                    .orElse(null);
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return AuthResponseDTO.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .profileId(profileId)
                .build();
    }
}
