package com.ankit.HealthCare_Backend.communication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class contactusDto {
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Email cannot be blank")
    private String email;
    private String subject;
    private String message;
}
