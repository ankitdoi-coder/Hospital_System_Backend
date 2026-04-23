package com.ankit.HealthCare_Backend.usermanagement.patient.service;

import java.util.List;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;



public interface PatientService {
    List<DoctorDTO> getAllDoctors();
    AppointmentDTO newAppointment(AppointmentDTO appointmentDTO, String patientEmail);
    List<AppointmentDTO> getMyAppointments();
    List<PrescriptionDTO> getMyPrescriptions();
    PatientDTO getMyProfile();
    void makePayment(Long appointmentId);
    void cancelAppointment(Long appointmentId);
}
