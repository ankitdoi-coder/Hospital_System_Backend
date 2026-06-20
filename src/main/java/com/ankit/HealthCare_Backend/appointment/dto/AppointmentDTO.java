package com.ankit.HealthCare_Backend.appointment.dto;

import java.time.LocalDate;

import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentDTO {
    private Long id;

    
    private Long patientId;

   
    private Long doctorId;

  
    private String doctorFirstName;

  
    private String doctorLastName;

   
    private String patientFirstName;
   
    private String patientLastName;
    
    private String doctorSpecialty;

    
    private LocalDate appointmentDate;

    private AppointmentStatusEnum status;
    private BillingStatus billingStatus;
    private Integer amount;
}