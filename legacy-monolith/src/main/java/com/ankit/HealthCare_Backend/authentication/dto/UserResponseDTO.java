package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "User registration response")
public class UserResponseDTO {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User email", example = "john@gmail.com")
    private String email;

    @Schema(description = "Assigned role name", example = "PATIENT")
    private String roleName;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Doctor specialty (null for patients)", example = "Cardiologist")
    private String specialty;

    @Schema(description = "Contact number (null for doctors)", example = "9876543210")
    private String contactNumber;

    @Schema(description = "Date of birth (null for doctors)", example = "1995-06-15")
    private LocalDate dob;

    @Schema(description = "Status message — doctors need admin approval before login", example = "Registration successful. Await admin approval.")
    private String message;
}
