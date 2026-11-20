package com.ankit.HealthCare_Backend.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Generic message response")
public class MessageResponseDTO {

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;
}
