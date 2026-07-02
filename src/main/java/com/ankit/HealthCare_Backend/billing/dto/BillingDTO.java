package com.ankit.HealthCare_Backend.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;
import lombok.Data;

@Data
@Schema(description = "Billing record for an appointment")
public class BillingDTO {

    @Schema(description = "Billing record ID", example = "1")
    private Long id;

    @Schema(description = "Associated appointment ID", example = "5")
    private Long appointmentId;

    @Schema(description = "Appointment fee in rupees", example = "500")
    private int amount;

    @Schema(description = "Payment status — UNPAID or PAID", example = "UNPAID")
    private BillingStatus billingStatus;
}
