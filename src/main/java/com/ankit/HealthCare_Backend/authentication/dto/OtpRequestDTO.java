package com.ankit.HealthCare_Backend.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequestDTO {
    @NotBlank @Email
    private String email;
}
