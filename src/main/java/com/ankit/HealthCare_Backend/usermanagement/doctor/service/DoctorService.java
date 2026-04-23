package com.ankit.HealthCare_Backend.usermanagement.doctor.service;

import java.util.List;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.appointment.dto.UpdateStatusDTO;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface DoctorService {
    List<AppointmentDTO> myUpcomingAppointments();
    List<PatientDTO> getMyPatients();
    PrescriptionDTO createPrescription(@RequestBody PrescriptionDTO prescriptionDTO);
    AppointmentDTO updateAppointmentStatus(Long id, UpdateStatusDTO status);
    List<PrescriptionDTO> getMyPrescriptions();
}
