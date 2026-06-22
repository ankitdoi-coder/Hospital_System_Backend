package com.ankit.HealthCare_Backend.authentication.repository;

import com.ankit.HealthCare_Backend.authentication.entity.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {
    Optional<EmailOtp> findTopByEmailOrderByExpiryTimeDesc(String email);
    void deleteByEmail(String email);
}
