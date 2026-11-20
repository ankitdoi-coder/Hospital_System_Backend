package com.healthcare.appointment;

import com.healthcare.appointment.client.UserClientFallback;
import com.healthcare.appointment.dto.AppointmentRequestDTO;
import com.healthcare.appointment.dto.DoctorDTO;
import com.healthcare.appointment.enums.AppointmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentServiceSmokeTest {

    @Test
    @DisplayName("Verify Resilience4j UserClientFallback behavior")
    void testResilienceFallback() {
        UserClientFallback fallback = new UserClientFallback();
        DoctorDTO doctor = fallback.getDoctorById(999L);

        assertThat(doctor).isNotNull();
        assertThat(doctor.getId()).isEqualTo(999L);
        assertThat(doctor.getFullName()).contains("Unavailable (Fallback)");
    }

    @Test
    @DisplayName("Verify Appointment request DTO and Status enum")
    void testAppointmentRequestDto() {
        AppointmentRequestDTO dto = AppointmentRequestDTO.builder()
                .patientId(1L)
                .doctorId(10L)
                .appointmentDate(LocalDate.now().plusDays(1))
                .appointmentTime(LocalTime.of(10, 30))
                .reasonForVisit("Routine Checkup")
                .status(AppointmentStatus.PENDING)
                .build();

        assertThat(dto.getDoctorId()).isEqualTo(10L);
        assertThat(dto.getReasonForVisit()).isEqualTo("Routine Checkup");
        assertThat(AppointmentStatus.PENDING.name()).isEqualTo("PENDING");
        assertThat(AppointmentStatus.SCHEDULED.name()).isEqualTo("SCHEDULED");
    }
}
