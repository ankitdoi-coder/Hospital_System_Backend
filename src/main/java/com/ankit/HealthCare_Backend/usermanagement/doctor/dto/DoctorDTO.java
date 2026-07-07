package com.ankit.HealthCare_Backend.usermanagement.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Doctor profile details")
public class DoctorDTO {

    @Schema(description = "Doctor ID", example = "1")
    private Long id;

    @Schema(description = "First name", example = "Dr. Sarah")
    private String firstName;

    @Schema(description = "Last name", example = "Smith")
    private String lastName;

    @Schema(description = "Medical specialty", example = "Cardiologist")
    private String specialty;

    @Schema(description = "Email address", example = "doctor@gmail.com")
    private String email;

    @Schema(description = "Years of experience", example = "8")
    private String experience;

    @Schema(description = "Whether the doctor is approved by admin to login and accept appointments", example = "true")
    private boolean isApproved;

    @Schema(description = "URL of the Doctor's profile picture", example = "https://res.cloudinary.com/.../profile.jpg")
    private String profilePicture;   
}
