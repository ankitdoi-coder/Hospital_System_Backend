package com.ankit.HealthCare_Backend.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerifyDTO {
    @NotBlank @Email
    private String email;

    @NotBlank
    private String otp;
}
