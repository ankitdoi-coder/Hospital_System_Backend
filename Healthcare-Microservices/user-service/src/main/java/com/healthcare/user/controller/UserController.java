package com.healthcare.user.controller;

import com.healthcare.user.dto.DoctorResponseDTO;
import com.healthcare.user.dto.PatientResponseDTO;
import com.healthcare.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Value("${server.port:8081}")
    private String runningPort;

    @GetMapping("/instance-info")
    public ResponseEntity<Map<String, Object>> getInstanceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "user-service");
        info.put("status", "UP");
        info.put("runningOnPort", runningPort);
        info.put("serverTime", LocalDateTime.now().toString());
        info.put("message", "Request routed successfully via API Gateway & LoadBalancer to User Service");
        return ResponseEntity.ok(info);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getPatientById(id));
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDoctorById(id));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(userService.getAllDoctors());
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(userService.getAllPatients());
    }
}
