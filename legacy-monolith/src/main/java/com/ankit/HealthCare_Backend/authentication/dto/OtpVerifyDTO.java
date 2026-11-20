package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "OTP verification request")
public class OtpVerifyDTO {

    @Schema(description = "Email address the OTP was sent to", example = "patient@gmail.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "6-digit OTP received in email", example = "482910")
    @NotBlank
    private String otp;
}
