package com.healthcare.user;

import com.healthcare.user.dto.DoctorResponseDTO;
import com.healthcare.user.dto.PatientResponseDTO;
import com.healthcare.user.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceSmokeTest {

    @Test
    @DisplayName("Verify Doctor and Patient DTO structures and Roles")
    void testDtoAndEnums() {
        DoctorResponseDTO doctor = DoctorResponseDTO.builder()
                .id(1L)
                .fullName("Dr. Smith")
                .specialization("Cardiology")
                .isApproved(true)
                .build();

        assertThat(doctor.getId()).isEqualTo(1L);
        assertThat(doctor.getFullName()).isEqualTo("Dr. Smith");
        assertThat(doctor.getSpecialization()).isEqualTo("Cardiology");
        assertThat(doctor.isApproved()).isTrue();

        PatientResponseDTO patient = PatientResponseDTO.builder()
                .id(2L)
                .fullName("John Doe")
                .phone("9876543210")
                .build();

        assertThat(patient.getId()).isEqualTo(2L);
        assertThat(patient.getFullName()).isEqualTo("John Doe");
        assertThat(Role.ROLE_DOCTOR.name()).isEqualTo("ROLE_DOCTOR");
        assertThat(Role.ROLE_PATIENT.name()).isEqualTo("ROLE_PATIENT");
    }
}
