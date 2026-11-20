package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Login request payload")
public class LoginRequestDTO {

    @Schema(description = "Registered email address", example = "patient@gmail.com")
    @NotNull(message = "Email cannot be null")
    @Email(message = "Enter a valid email")
    private String email;

    @Schema(description = "Account password", example = "MyPassword@123")
    @NotNull(message = "Password cannot be null")
    private String password;
}
