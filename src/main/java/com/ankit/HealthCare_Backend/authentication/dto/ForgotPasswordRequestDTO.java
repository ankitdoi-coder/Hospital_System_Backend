package com.ankit.HealthCare_Backend.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    private String email;
}