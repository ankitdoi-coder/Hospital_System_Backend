package com.ankit.HealthCare_Backend.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login response containing JWT token")
public class LoginResponseDTO {

    @Schema(description = "JWT token to use in Authorization header", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String jwt;

    @Schema(description = "Optional message", example = "Login successful")
    private String message;

    public LoginResponseDTO(String jwt) {
        this.jwt = jwt;
    }
}
