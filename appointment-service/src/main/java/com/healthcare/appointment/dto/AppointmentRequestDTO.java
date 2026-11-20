package com.healthcare.appointment.dto;

import com.healthcare.appointment.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {

    @NotNull(message = "Patient ID is mandatory")
    private Long patientId;

    @NotNull(message = "Doctor ID is mandatory")
    private Long doctorId;

    @NotNull(message = "Appointment date is mandatory")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is mandatory")
    private LocalTime appointmentTime;

    private String reasonForVisit;

    private AppointmentStatus status;
}
