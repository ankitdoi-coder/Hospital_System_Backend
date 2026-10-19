package com.healthcare.appointment.client;

import com.healthcare.appointment.dto.DoctorDTO;
import com.healthcare.appointment.dto.PatientDTO;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for UserClient
 * 
 * If User Service is down, slow, or returning 500 errors,
 * Resilience4j / Feign will automatically route calls here instead of crashing the thread!
 */
@Component
public class UserClientFallback implements UserClient {

    @Override
    public PatientDTO getPatientById(Long id) {
        return PatientDTO.builder()
                .id(id)
                .fullName("Unavailable (Fallback)")
                .email("n/a")
                .phone("n/a")
                .bloodGroup("n/a")
                .gender("n/a")
                .build();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) {
        return DoctorDTO.builder()
                .id(id)
                .fullName("Unavailable (Fallback)")
                .email("n/a")
                .specialization("General / Pending")
                .department("N/A")
                .consultationFee(0.0)
                .build();
    }
}
