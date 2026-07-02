package com.ankit.HealthCare_Backend.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request to update appointment status")
public class UpdateStatusDTO {

    @Schema(description = "New appointment status — allowed values: PENDING, SCHEDULED, COMPLETED, CANCELLED", example = "COMPLETED")
    @NotNull(message = "Status cannot be null")
    private AppointmentStatusEnum status;
}
