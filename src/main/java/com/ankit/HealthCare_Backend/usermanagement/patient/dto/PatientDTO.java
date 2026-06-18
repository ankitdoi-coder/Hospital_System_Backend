package com.ankit.HealthCare_Backend.usermanagement.patient.dto;

import java.time.LocalDate;
import  lombok.Data;


@Data
public class PatientDTO {
    private Long  id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Long contactNumber;
    private LocalDate dob;
    private String email;

    
}
