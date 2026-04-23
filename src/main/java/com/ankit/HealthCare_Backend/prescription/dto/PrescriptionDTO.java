package com.ankit.HealthCare_Backend.prescription.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PrescriptionDTO {
    private Long id;
    private Long appointmentId;
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;
    private String medicationDetails;
    private String dosages;
    private LocalDateTime createdAt;
}