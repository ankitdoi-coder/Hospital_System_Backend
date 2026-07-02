package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "OTP send request — provide email to receive OTP")
public class OtpRequestDTO {

    @Schema(description = "Email address to send OTP to", example = "patient@gmail.com")
    @NotBlank
    @Email
    private String email;
}
