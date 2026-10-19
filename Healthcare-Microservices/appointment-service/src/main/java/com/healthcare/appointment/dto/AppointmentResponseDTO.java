package com.healthcare.appointment.dto;

import com.healthcare.appointment.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reasonForVisit;
    private AppointmentStatus status;
    private LocalDateTime createdAt;

    // Enriched fields from User-Service (via OpenFeign)
    private PatientDTO patientDetails;
    private DoctorDTO doctorDetails;

    // Port info to demonstrate Load Balancing across multiple instances!
    private String servedByPort;
}
