package com.healthcare.appointment.controller;

import com.healthcare.appointment.dto.AppointmentRequestDTO;
import com.healthcare.appointment.dto.AppointmentResponseDTO;
import com.healthcare.appointment.enums.AppointmentStatus;
import com.healthcare.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Value("${server.port:8082}")
    private String runningPort;

    /**
     * Endpoint to demonstrate and verify Client-Side Load Balancing
     * When hitting Gateway /api/appointments/instance-info multiple times,
     * the response will alternate between ports of running instances!
     */
    @GetMapping("/instance-info")
    public ResponseEntity<Map<String, Object>> getInstanceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "appointment-service");
        info.put("status", "UP");
        info.put("runningOnPort", runningPort);
        info.put("serverTime", LocalDateTime.now().toString());
        info.put("message", "Request successfully handled by Spring Cloud LoadBalancer via Eureka!");
        return ResponseEntity.ok(info);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        AppointmentResponseDTO response = appointmentService.createAppointment(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorId(doctorId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, status));
    }
}
