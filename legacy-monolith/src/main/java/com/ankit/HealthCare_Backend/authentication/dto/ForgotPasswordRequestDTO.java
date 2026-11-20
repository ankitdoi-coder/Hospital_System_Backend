package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Forgot password request — provide registered email to receive reset link")
public class ForgotPasswordRequestDTO {

    @Schema(description = "Registered email address", example = "patient@gmail.com")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    private String email;
}
