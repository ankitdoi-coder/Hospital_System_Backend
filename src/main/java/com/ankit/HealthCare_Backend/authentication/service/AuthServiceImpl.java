package com.ankit.HealthCare_Backend.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import com.ankit.HealthCare_Backend.usermanagement.doctor.repository.DoctorRepository;
import com.ankit.HealthCare_Backend.core.entity.Role;
import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import com.ankit.HealthCare_Backend.usermanagement.patient.repository.PatientRepository;
import com.ankit.HealthCare_Backend.core.repository.RoleRepository;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.PasswordResetToken;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.PasswordResetTokenRepository;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;
import com.ankit.HealthCare_Backend.usermanagement.user.repository.UserRepository;
import com.ankit.HealthCare_Backend.authentication.dto.UserResponseDTO;
import com.ankit.HealthCare_Backend.authentication.dto.RegisterRequestDTO;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private PatientRepository patientRepo;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepo;

    @Override
    @Transactional
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequest) {
        Role userRole = roleRepo.findById(registerRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        
        // Check if the user already exists
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        // Part 1: Create the User record
        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(userRole);
        newUser.setApproved(true); //will be always true for patient
        User savedUser = userRepo.save(newUser);

        // Create a basic response object first
        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setRoleName(savedUser.getRole().getName());
        
        // Part 2: Create the Doctor or Patient profile and add details to the response
        if ("DOCTOR".equalsIgnoreCase(userRole.getName())) {
            Doctor newDoctor = new Doctor();
            newDoctor.setFirstName(registerRequest.getFirstName());
            newDoctor.setLastName(registerRequest.getLastName());
            newDoctor.setSpecialty(registerRequest.getSpecialty());
            newDoctor.setUser(savedUser);
            newDoctor.setApproved(false);
            newDoctor.setExperience(registerRequest.getExperience()); // set experience
            doctorRepo.save(newDoctor);

            // Add doctor-specific details to the response
            response.setFirstName(newDoctor.getFirstName());
            response.setLastName(newDoctor.getLastName());
            response.setSpecialty(newDoctor.getSpecialty());
            response.setMessage("Approval request sent to admin!");

        } else if ("PATIENT".equalsIgnoreCase(userRole.getName())) {
            Patient newPatient = new Patient();
            newPatient.setFirstName(registerRequest.getFirstName());
            newPatient.setLastName(registerRequest.getLastName());
            newPatient.setContactNumber(registerRequest.getContactNumber());
            newPatient.setDob(registerRequest.getDob());
            newPatient.setUser(savedUser);
            patientRepo.save(newPatient);

            // Add patient-specific details to the response
            response.setFirstName(newPatient.getFirstName());
            response.setLastName(newPatient.getLastName());
            response.setContactNumber(newPatient.getContactNumber());
            response.setDob(newPatient.getDob());
            response.setMessage("Registration successful!");
        }

        // ✅ This is the final, guaranteed return statement
        return response;
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        // Check if user is a patient or doctor (not admin)
        String roleName = user.getRole().getName();
        if (!"PATIENT".equalsIgnoreCase(roleName) && !"DOCTOR".equalsIgnoreCase(roleName)) {
            throw new RuntimeException("Password reset is only available for patients and doctors");
        }

        // Delete any existing tokens for this user
        passwordResetTokenRepo.deleteByUserId(user.getId());

        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepo.save(resetToken);

        // In a real application, you would send an email here
        // For now, we'll return the token (in production, this should be sent via email)
        return "Password reset token generated. Token: " + token + " (In production, this would be sent via email)";
    }

    @Override
    @Transactional
    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        // Check if token is expired
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        // Check if token is already used
        if (resetToken.isUsed()) {
            throw new RuntimeException("Reset token has already been used");
        }

        // Update user password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        passwordResetTokenRepo.save(resetToken);

        return "Password has been reset successfully";
    }
}
