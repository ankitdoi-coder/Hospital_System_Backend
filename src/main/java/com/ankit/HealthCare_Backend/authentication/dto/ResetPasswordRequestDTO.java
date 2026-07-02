package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Reset password request using token from email")
public class ResetPasswordRequestDTO {

    @Schema(description = "Password reset token received in email", example = "abc123xyz...")
    @NotBlank(message = "Token cannot be blank")
    private String token;

    @Schema(description = "New password (min 6 characters)", example = "NewPassword@123")
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}
