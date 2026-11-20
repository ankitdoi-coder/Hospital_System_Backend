package com.ankit.HealthCare_Backend.prescription.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ankit.HealthCare_Backend.prescription.entity.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,Long>{
    List<Prescription> findByAppointmentId(Long appointmentId);
}