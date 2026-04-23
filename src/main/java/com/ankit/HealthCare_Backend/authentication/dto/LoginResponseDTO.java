package com.ankit.HealthCare_Backend.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String jwt; 
    private String message;

    public LoginResponseDTO(String jwt) {
        this.jwt = jwt;
    }
}