package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to verify if a password reset token is still valid")
public class VerifyResetTokenRequestDTO {

    @Schema(description = "Email address associated with the reset request", example = "patient@gmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Reset token received in email", example = "abc123xyz...")
    @NotBlank(message = "Token is required")
    private String token;
}
