package com.ankit.HealthCare_Backend.authentication.dto;

import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;


//this is the Type of Input we will take from the user Like Empty Object Type of this
@Data
public class RegisterRequestDTO {
    @NotNull(message = "Email cannot be null")
    @NotBlank(message="Email can not be Blank")
    @Email(message="Enter a valid email")
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message="Password can not be Blank")
    private String password;


    private Long roleId;
    // New fields for Doctor/Patient profiles
    private String firstName;
    private String lastName;
    private String specialty;

    @NotNull(message = "Contact number cannot be null")
    @Digits(integer=10,fraction=0,message="Phone must be exactly 10 Digits")
    private Long contactNumber;
    private LocalDate dob;
    private int experience; // in years
}