package com.ankit.HealthCare_Backend.usermanagement.doctor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.appointment.dto.UpdateStatusDTO;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.service.DoctorService;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;

@Tag(name = "Doctor", description = "Doctor operations — requires DOCTOR role JWT token")
@SecurityRequirement(name = "Bearer Auth")
@RestController
@RequestMapping("/api/doctor")
@Validated
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @Operation(summary = "Get my profile", description = "Returns the logged-in doctor's profile details")
    @ApiResponse(responseCode = "200", description = "Doctor profile returned")
    @GetMapping("/profile")
    public ResponseEntity<DoctorDTO> getMyProfile() {
        return ResponseEntity.ok(doctorService.getMyProfile());
    }

    @Operation(summary = "Get my appointments", description = "Returns all appointments assigned to the logged-in doctor — includes PENDING, SCHEDULED, COMPLETED, CANCELLED")
    @ApiResponse(responseCode = "200", description = "Appointment list returned")
    @GetMapping("/appointments/my")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingAppointments() {
        List<AppointmentDTO> appointments = doctorService.myUpcomingAppointments();
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Get my patients", description = "Returns all patients who have at least one appointment with this doctor")
    @ApiResponse(responseCode = "200", description = "Patient list returned")
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getMyPatients() {
        List<PatientDTO> patients = doctorService.getMyPatients();
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Update appointment status", description = "Changes the status of an appointment — e.g. PENDING → SCHEDULED → COMPLETED")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointment status updated"),
        @ApiResponse(responseCode = "404", description = "Appointment not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(
            @Parameter(description = "Appointment ID", required = true, example = "1")
            @Positive(message = "Appointment ID must be positive") @PathVariable Long id,
            @Valid @RequestBody UpdateStatusDTO statusDTO) {
        AppointmentDTO updatedAppointment = doctorService.updateAppointmentStatus(id, statusDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    @Operation(summary = "Create a prescription", description = "Creates a prescription for a completed appointment. Appointment must be in COMPLETED status")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Prescription created successfully"),
        @ApiResponse(responseCode = "400", description = "Appointment not completed or validation error"),
        @ApiResponse(responseCode = "404", description = "Appointment or patient not found")
    })
    @PostMapping("/prescription")
    public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        PrescriptionDTO createdPrescription = doctorService.createPrescription(prescriptionDTO);
        return ResponseEntity.ok(createdPrescription);
    }

    @Operation(summary = "Get my prescriptions", description = "Returns all prescriptions created by the logged-in doctor")
    @ApiResponse(responseCode = "200", description = "Prescription list returned")
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDTO>> getMyPrescriptions() {
        List<PrescriptionDTO> prescriptions = doctorService.getMyPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }
}
