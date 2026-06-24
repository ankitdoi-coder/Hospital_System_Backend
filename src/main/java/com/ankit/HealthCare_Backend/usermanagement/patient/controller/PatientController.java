package com.ankit.HealthCare_Backend.usermanagement.patient.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/patient")
@Validated
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    // gets the List of the doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        List<DoctorDTO> doctors = patientService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    // Book a new Appointment
    @PostMapping("/appointments/new")
    public ResponseEntity<AppointmentDTO> newAppointment(
           @Valid @RequestBody AppointmentDTO appointmentDTO,
            Authentication authentication) {

        // Get patient email from JWT
        String patientEmail = authentication.getName();

        // Pass email to service to find patient
        AppointmentDTO newAppointment = patientService.newAppointment(appointmentDTO, patientEmail);
        return ResponseEntity.ok(newAppointment);
    }

    // View personal appointment history
    @GetMapping("/appointments/my")
    public ResponseEntity<List<AppointmentDTO>> myAppointments() {
        List<AppointmentDTO> appointments = patientService.getMyAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Get my prescriptions
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDTO>> myPrescriptions() {
        List<PrescriptionDTO> prescriptions = patientService.getMyPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    // Get my profile
    @GetMapping("/profile")
    public ResponseEntity<PatientDTO> myProfile() {
        PatientDTO profile = patientService.getMyProfile();
        return ResponseEntity.ok(profile);
    }

    // Make payment
    @PutMapping("/appointments/{id}/pay")
    public ResponseEntity<String> makePayment(@Positive(message = "Appointment ID must be positive") @PathVariable Long id) {
        patientService.makePayment(id);
        return ResponseEntity.ok("Payment successful");
    }

    // Cancel appointment
    @DeleteMapping("/appointments/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(@Positive(message = "Appointment ID must be positive") @PathVariable Long id) {
        patientService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }
}
