package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Digits;

@Data
@Schema(description = "Registration request payload. roleId: 2 = Patient, 3 = Doctor")
public class RegisterRequestDTO {

    @Schema(description = "User email address", example = "john@gmail.com")
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email can not be Blank")
    @Email(message = "Enter a valid email")
    private String email;

    @Schema(description = "Password (min 6 characters)", example = "MyPassword@123")
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password can not be Blank")
    private String password;

    @Schema(description = "Role ID — 2 for Patient, 3 for Doctor", example = "2")
    private Long roleId;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Doctor specialty (only for Doctor role)", example = "Cardiologist")
    private String specialty;

    @Schema(description = "10-digit contact number", example = "9876543210")
    @NotBlank(message = "Contact number cannot be empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits")
    private String contactNumber;

    @Schema(description = "Date of birth", example = "1995-06-15")
    private LocalDate dob;

    @Schema(description = "Years of experience (only for Doctor role)", example = "5")
    private String experience;
}
