package com.ankit.HealthCare_Backend.prescription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Prescription details issued by a doctor")
public class PrescriptionDTO {

    @Schema(description = "Prescription ID (auto-generated)", example = "1")
    private Long id;

    @Schema(description = "Appointment ID this prescription belongs to — required", example = "5")
    @NotNull(message = "Appointment ID cannot be null")
    private Long appointmentId;

    @Schema(description = "Patient ID — required", example = "2")
    private Long patientId;

    @Schema(description = "Patient first name", example = "John")
    private String patientFirstName;

    @Schema(description = "Patient last name", example = "Doe")
    private String patientLastName;

    @Schema(description = "Medication details prescribed by the doctor", example = "Paracetamol 500mg, Amoxicillin 250mg")
    @NotBlank(message = "Medication details cannot be blank")
    private String medicationDetails;

    @Schema(description = "Dosage instructions", example = "Paracetamol: 1 tablet twice daily. Amoxicillin: 1 capsule three times daily")
    @NotBlank(message = "Dosages cannot be blank")
    private String dosages;

    @Schema(description = "Prescription creation timestamp", example = "2025-08-20T10:30:00")
    private LocalDateTime createdAt;
}
