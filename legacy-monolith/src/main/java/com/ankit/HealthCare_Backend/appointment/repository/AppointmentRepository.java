package com.ankit.HealthCare_Backend.appointment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ankit.HealthCare_Backend.appointment.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // in AppointmentRepository (extends JpaRepository<Appointment, Long>)
    List<Appointment> findByPatientId(Long patientId);

    // 1. Pagination ke liye (Jo tumne abhi add kiya tha)
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);

    // 2. Bina Pagination ke liye (Ye line wapas add kar do taaki purana code na
    // fate)
    List<Appointment> findByDoctorId(Long doctorId);

    Appointment findByPatientIdAndDoctorIdAndAppointmentDate(Long patientId, Long doctorId,
            java.time.LocalDate appointmentDate);

    Appointment findByIdAndDoctorId(Long id, Long doctorId);
}