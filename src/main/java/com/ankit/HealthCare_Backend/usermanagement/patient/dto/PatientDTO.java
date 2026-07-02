package com.ankit.HealthCare_Backend.usermanagement.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Data;

@Data
@Schema(description = "Patient profile details")
public class PatientDTO {

    @Schema(description = "Patient ID", example = "1")
    private Long id;

    @Schema(description = "Associated user account ID", example = "5")
    private Long userId;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "10-digit contact number", example = "9876543210")
    private Long contactNumber;

    @Schema(description = "Date of birth", example = "1995-06-15")
    private LocalDate dob;

    @Schema(description = "Email address", example = "john@gmail.com")
    private String email;
}
