// In your DTO package
package com.ankit.HealthCare_Backend.usermanagement.doctor.dto;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String email;
    private int experience; // in years
    private boolean isApproved;
}