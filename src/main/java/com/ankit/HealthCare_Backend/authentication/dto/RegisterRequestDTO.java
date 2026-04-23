package com.ankit.HealthCare_Backend.authentication.dto;

import lombok.Data;

import java.time.LocalDate;

//this is the Type of Input we will take from the user Like Empty Object Type of this
@Data
public class RegisterRequestDTO {
    
    private String email;
    private String password;
    private Long roleId;
    // New fields for Doctor/Patient profiles
    private String firstName;
    private String lastName;
    private String specialty;
    private Long contactNumber;
    private LocalDate dob;
    private int experience; // in years
}