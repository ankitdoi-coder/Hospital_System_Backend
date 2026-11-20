package com.ankit.HealthCare_Backend.billing.entity;

import com.ankit.HealthCare_Backend.Audits.BaseAuditEntity;
import com.ankit.HealthCare_Backend.appointment.entity.Appointment;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billing extends BaseAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Appointment Id",nullable = false)
    private Appointment appointment_id;
    
    @Column(name = "Amount",nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "Billing Status",nullable = false)
    private BillingStatus billing_status;   
}