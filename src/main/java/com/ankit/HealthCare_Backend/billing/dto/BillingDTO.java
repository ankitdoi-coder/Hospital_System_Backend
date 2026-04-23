package com.ankit.HealthCare_Backend.billing.dto;

import com.ankit.HealthCare_Backend.core.enums.BillingStatus;

import lombok.Data;

@Data
public class BillingDTO {
    private Long id;
    private Long appointmentId;
    private int amount;
    private BillingStatus billingStatus;
}