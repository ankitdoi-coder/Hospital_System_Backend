package com.ankit.HealthCare_Backend.usermanagement.patient.repository;

import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>{
	// Find a Patient record based on the linked User id
	Patient findByUserId(Long userId);

	// Page<Patient> findAll(Pageable pageable);
	
}
