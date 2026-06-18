package com.ankit.HealthCare_Backend.appointment.entity;

import java.time.LocalDate;

import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import com.ankit.HealthCare_Backend.core.enums.AppointmentStatusEnum;
import com.ankit.HealthCare_Backend.Notification.NotificationEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name="notification")
    private NotificationEntity notification;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppointmentStatusEnum status; // Enum: SCHEDULED, COMPLETED, CANCELED
}