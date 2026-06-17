package com.ankit.HealthCare_Backend.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;
}