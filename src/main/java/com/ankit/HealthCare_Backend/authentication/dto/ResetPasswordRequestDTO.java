package com.ankit.HealthCare_Backend.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Token cannot be blank")
    private String token;

    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}