package com.ankit.HealthCare_Backend.Payments;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import com.ankit.HealthCare_Backend.usermanagement.patient.service.PatientService;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class RazorpayController {

    private final RazorpayService razorpayService;
    private final PatientService patientService;

    public RazorpayController(RazorpayService razorpayService, PatientService patientService) {
        this.razorpayService = razorpayService;
        this.patientService = patientService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> body) throws Exception {
        //extracts the amount from the map of Body using key "amount"
        int amount = (int) body.getOrDefault("amount", 0);
        String currency = (String) body.getOrDefault("currency", "INR");
        log.info("Received create-order request: amount={}, currency={}", amount, currency);
        Map<String, Object> resp = razorpayService.createOrder(amount, currency);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> payload) throws Exception {
        log.info("Received verify request with keys: {}", payload.keySet());

        String appointmentIdStr = payload.get("appointmentId");
        if (appointmentIdStr == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "appointmentId is required"));
        }

        Long appointmentId = Long.valueOf(appointmentIdStr);

        razorpayService.verifySignature(payload);

        // Mark appointment as paid
        patientService.makePayment(appointmentId);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
