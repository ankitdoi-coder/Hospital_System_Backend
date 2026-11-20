package com.healthcare.appointment.service;

import com.healthcare.appointment.client.UserClient;
import com.healthcare.appointment.dto.AppointmentRequestDTO;
import com.healthcare.appointment.dto.AppointmentResponseDTO;
import com.healthcare.appointment.dto.DoctorDTO;
import com.healthcare.appointment.dto.PatientDTO;
import com.healthcare.appointment.entity.Appointment;
import com.healthcare.appointment.enums.AppointmentStatus;
import com.healthcare.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserClient userClient;

    @Value("${server.port:8082}")
    private String runningPort;

    @Override
    @Transactional
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO) {
        log.info("Creating appointment on instance running on port {}", runningPort);

        // 1. Validate / fetch patient and doctor via Feign client
        PatientDTO patient = userClient.getPatientById(requestDTO.getPatientId());
        DoctorDTO doctor = userClient.getDoctorById(requestDTO.getDoctorId());

        // 2. Save appointment in local DB
        Appointment appointment = Appointment.builder()
                .patientId(requestDTO.getPatientId())
                .doctorId(requestDTO.getDoctorId())
                .appointmentDate(requestDTO.getAppointmentDate())
                .appointmentTime(requestDTO.getAppointmentTime())
                .reasonForVisit(requestDTO.getReasonForVisit())
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : AppointmentStatus.SCHEDULED)
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        // 3. Construct enriched response DTO
        return mapToResponse(saved, patient, doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));

        PatientDTO patient = userClient.getPatientById(appointment.getPatientId());
        DoctorDTO doctor = userClient.getDoctorById(appointment.getDoctorId());

        return mapToResponse(appointment, patient, doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(apt -> {
                    PatientDTO patient = userClient.getPatientById(apt.getPatientId());
                    DoctorDTO doctor = userClient.getDoctorById(apt.getDoctorId());
                    return mapToResponse(apt, patient, doctor);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(apt -> {
                    PatientDTO patient = userClient.getPatientById(apt.getPatientId());
                    DoctorDTO doctor = userClient.getDoctorById(apt.getDoctorId());
                    return mapToResponse(apt, patient, doctor);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponseDTO updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));

        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);

        PatientDTO patient = userClient.getPatientById(updated.getPatientId());
        DoctorDTO doctor = userClient.getDoctorById(updated.getDoctorId());

        return mapToResponse(updated, patient, doctor);
    }

    private AppointmentResponseDTO mapToResponse(Appointment apt, PatientDTO patient, DoctorDTO doctor) {
        return AppointmentResponseDTO.builder()
                .id(apt.getId())
                .patientId(apt.getPatientId())
                .doctorId(apt.getDoctorId())
                .appointmentDate(apt.getAppointmentDate())
                .appointmentTime(apt.getAppointmentTime())
                .reasonForVisit(apt.getReasonForVisit())
                .status(apt.getStatus())
                .createdAt(apt.getCreatedAt())
                .patientDetails(patient)
                .doctorDetails(doctor)
                .servedByPort(runningPort)
                .build();
    }
}
