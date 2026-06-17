package com.ankit.HealthCare_Backend.appointment.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;


import lombok.Data;

@Data
public class AppointmentDTO {
    private Long id;

    @NotNull(message="patient Id can not be null")
    private Long patientId;

    @NotNull(message="doctor id can not be null")
    private Long doctorId;

    @NotNull(message="doctor first name can not be null")
    private String doctorFirstName;

    @NotNull(message="doctor lastName can not be null")
    private String doctorLastName;

    @NotNull(message ="patient first name can not be null")
    private String patientFirstName;
    @NotNull(message="patient lastnamecan not be null")
    private String patientLastName;
    
    private String doctorSpecialty;

    @NotNull(message="appointment date can not be null ")
    private LocalDate appointmentDate;

    private AppointmentStatusEnum status;
    private BillingStatus billingStatus;
    private Integer amount;
}