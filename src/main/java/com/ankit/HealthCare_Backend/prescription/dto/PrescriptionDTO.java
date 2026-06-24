package com.ankit.HealthCare_Backend.prescription.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionDTO {
    private Long id;

    @NotNull(message = "Appointment ID cannot be null")
    private Long appointmentId;

    @NotNull(message = "Patient ID cannot be null")
    private Long patientId;

    private String patientFirstName;
    private String patientLastName;

    @NotBlank(message = "Medication details cannot be blank")
    private String medicationDetails;

    @NotBlank(message = "Dosages cannot be blank")
    private String dosages;

    private LocalDateTime createdAt;
}