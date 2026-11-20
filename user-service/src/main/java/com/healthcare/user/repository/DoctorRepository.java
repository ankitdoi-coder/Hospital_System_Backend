package com.healthcare.user.repository;

import com.healthcare.user.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    List<Doctor> findByIsApprovedTrue();
    List<Doctor> findBySpecializationIgnoreCase(String specialization);
}
