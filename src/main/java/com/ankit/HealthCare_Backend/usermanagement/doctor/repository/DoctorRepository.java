package com.ankit.HealthCare_Backend.usermanagement.doctor.repository;

import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,Long>{
    Doctor findByUserId(Long userId);
}
