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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import com.ankit.HealthCare_Backend.appointment.dto.AppointmentDTO;
import com.ankit.HealthCare_Backend.appointment.dto.UpdateStatusDTO;
import com.ankit.HealthCare_Backend.prescription.dto.PrescriptionDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.service.DoctorService;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;



@RestController
@RequestMapping("/api/doctor")
@Validated
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    // Get doctor's own profile
    @GetMapping("/profile")
    public ResponseEntity<DoctorDTO> getMyProfile() {
        return ResponseEntity.ok(doctorService.getMyProfile());
    }

    // get the upcoming all appoinments Pending,Schedulled,Completed etc
    @GetMapping("/appointments/my")
    public ResponseEntity<List<AppointmentDTO>> getUpcomingAppointments() {
        List<AppointmentDTO> appointments = doctorService.myUpcomingAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Get all patients who have appointments with this doctor
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getMyPatients() {
        List<PatientDTO> patients = doctorService.getMyPatients();
        return ResponseEntity.ok(patients);
    }

    // change appointment Status
    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<AppointmentDTO> updateAppointmentStatus(
            @Positive(message = "Appointment ID must be positive") @PathVariable Long id,
            @Valid @RequestBody UpdateStatusDTO statusDTO) {

        AppointmentDTO updatedAppointment = doctorService.updateAppointmentStatus(id, statusDTO);
        return ResponseEntity.ok(updatedAppointment);
    }

    // Create a new prescription for a completed appointment.
    @PostMapping("/prescription")
    public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        PrescriptionDTO createdPrescription = doctorService.createPrescription(prescriptionDTO);
        return ResponseEntity.ok(createdPrescription);
    }

    // Get all prescriptions created by this doctor
    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDTO>> getMyPrescriptions() {
        List<PrescriptionDTO> prescriptions = doctorService.getMyPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }
}
