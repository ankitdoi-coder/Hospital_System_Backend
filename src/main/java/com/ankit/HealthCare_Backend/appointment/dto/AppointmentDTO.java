package com.ankit.HealthCare_Backend.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;
import lombok.Data;

@Data
@Schema(description = "Appointment details")
public class AppointmentDTO {

    @Schema(description = "Appointment ID (auto-generated)", example = "1")
    private Long id;

    @Schema(description = "Patient ID", example = "2")
    private Long patientId;

    @Schema(description = "Doctor ID — required when booking", example = "3")
    private Long doctorId;

    @Schema(description = "Doctor first name", example = "Sarah")
    private String doctorFirstName;

    @Schema(description = "Doctor last name", example = "Smith")
    private String doctorLastName;

    @Schema(description = "Patient first name", example = "John")
    private String patientFirstName;

    @Schema(description = "Patient last name", example = "Doe")
    private String patientLastName;

    @Schema(description = "Doctor specialty", example = "Cardiologist")
    private String doctorSpecialty;

    @Schema(description = "Appointment date — required when booking", example = "2025-08-20")
    private LocalDate appointmentDate;

    @Schema(description = "Appointment time — required when booking", example = "10:30:00")
    private LocalTime appointmentTime;

    @Schema(description = "Reason for visit", example = "Chest pain and shortness of breath")
    private String reasonForVisit;

    @Schema(description = "Appointment status", example = "PENDING")
    private AppointmentStatusEnum status;

    @Schema(description = "Billing/payment status", example = "UNPAID")
    private BillingStatus billingStatus;

    @Schema(description = "Appointment fee in rupees", example = "500")
    private Integer amount;
}
