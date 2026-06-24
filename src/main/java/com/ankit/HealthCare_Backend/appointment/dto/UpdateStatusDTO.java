package com.ankit.HealthCare_Backend.appointment.dto;

import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusDTO {
    @NotNull(message = "Status cannot be null")
    private AppointmentStatusEnum status;
}