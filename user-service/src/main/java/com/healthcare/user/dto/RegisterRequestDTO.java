package com.healthcare.user.dto;

import com.healthcare.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String fullName;
    private String firstName;
    private String lastName;

    private String phone;
    private String contactNumber;

    private Role role; // ROLE_PATIENT or ROLE_DOCTOR
    private Integer roleId; // 1 = Patient, 2 = Doctor

    // Extra details if Doctor
    private String specialization;
    private String specialty;
    private String department;
    private Double consultationFee;
    private Integer experience;

    // Extra details if Patient
    private String bloodGroup;
    private String gender;
    private String dob;

    public String getFullName() {
        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }
        if (firstName != null || lastName != null) {
            String combined = ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();
            if (!combined.isEmpty()) return combined;
        }
        return "User";
    }

    public String getPhone() {
        if (phone != null && !phone.isBlank()) {
            return phone;
        }
        return contactNumber;
    }

    public Role getRole() {
        if (role != null) {
            return role;
        }
        if (roleId != null) {
            return roleId == 2 ? Role.ROLE_DOCTOR : Role.ROLE_PATIENT;
        }
        return Role.ROLE_PATIENT;
    }

    public String getSpecialization() {
        if (specialization != null && !specialization.isBlank()) {
            return specialization;
        }
        return specialty;
    }
}
