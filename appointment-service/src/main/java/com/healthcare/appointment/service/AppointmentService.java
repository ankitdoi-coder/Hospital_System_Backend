package com.healthcare.appointment.service;

import com.healthcare.appointment.dto.AppointmentRequestDTO;
import com.healthcare.appointment.dto.AppointmentResponseDTO;
import com.healthcare.appointment.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {
    AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO);
    AppointmentResponseDTO getAppointmentById(Long id);
    List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId);
    List<AppointmentResponseDTO> getAppointmentsByDoctorId(Long doctorId);
    AppointmentResponseDTO updateAppointmentStatus(Long id, AppointmentStatus status);
}
