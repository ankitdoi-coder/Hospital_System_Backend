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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Tag(name = "Patient", description = "Patient operations — requires PATIENT role JWT token")
@SecurityRequirement(name = "Bearer Auth")
@RestController
@RequestMapping("/api/patient")
@Validated
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @Operation(summary = "Get all approved doctors", description = "Returns list of all doctors approved by admin — used to select a doctor when booking appointment")
    @ApiResponse(responseCode = "200", description = "List of doctors returned")
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getDoctors() {
        List<DoctorDTO> doctors = patientService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Book a new appointment", description = "Books an appointment with a doctor. Pass doctorId and appointmentDate in the request body")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment booked successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error in request body"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @PostMapping("/appointments/new")
    public ResponseEntity<AppointmentDTO> newAppointment(
            @Valid @RequestBody AppointmentDTO appointmentDTO,
            Authentication authentication) {
        String patientEmail = authentication.getName();
        AppointmentDTO newAppointment = patientService.newAppointment(appointmentDTO, patientEmail);
        return ResponseEntity.ok(newAppointment);
    }

    @Operation(summary = "Get my appointment history", description = "Returns all appointments (past and upcoming) for the logged-in patient")
    @ApiResponse(responseCode = "200", description = "Appointment list returned")
    @GetMapping("/appointments/my")
    public ResponseEntity<List<AppointmentDTO>> myAppointments() {
        List<AppointmentDTO> appointments = patientService.getMyAppointments();
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Get my prescriptions", description = "Returns all prescriptions issued to the logged-in patient")
    @ApiResponse(responseCode = "200", description = "Prescription list returned")
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDTO>> myPrescriptions() {
        List<PrescriptionDTO> prescriptions = patientService.getMyPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @Operation(summary = "Get my profile", description = "Returns the logged-in patient's profile details")
    @ApiResponse(responseCode = "200", description = "Patient profile returned")
    @GetMapping("/profile")
    public ResponseEntity<PatientDTO> myProfile() {
        PatientDTO profile = patientService.getMyProfile();
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Make payment for appointment", description = "Marks the billing status of an appointment as PAID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment successful"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PutMapping("/appointments/{id}/pay")
    public ResponseEntity<String> makePayment(
            @Parameter(description = "Appointment ID", required = true, example = "1") @Positive(message = "Appointment ID must be positive") @PathVariable Long id) {
        patientService.makePayment(id);
        return ResponseEntity.ok("Payment successful");
    }

    @Operation(summary = "Cancel an appointment", description = "Cancels a pending or scheduled appointment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @DeleteMapping("/appointments/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(
            @Parameter(description = "Appointment ID", required = true, example = "1") @Positive(message = "Appointment ID must be positive") @PathVariable Long id) {
        patientService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }

    @Operation(summary = "Update patient profile", description = "Updates the logged-in patient's personal details")
    @PutMapping("/profile")
    public ResponseEntity<PatientDTO> updateProfile(@RequestBody PatientDTO patientDTO, Authentication authentication) {
        String email = authentication.getName();
        PatientDTO updated = patientService.updateProfile(patientDTO, email);
        return ResponseEntity.ok(updated);
    }
}
