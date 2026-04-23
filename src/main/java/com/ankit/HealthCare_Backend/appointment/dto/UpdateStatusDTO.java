package com.ankit.HealthCare_Backend.appointment.dto;

import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import lombok.Data;

@Data
public class UpdateStatusDTO {
    private AppointmentStatusEnum status;
}