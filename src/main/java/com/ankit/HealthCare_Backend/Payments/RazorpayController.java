package com.ankit.HealthCare_Backend.Payments;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Map;

import com.ankit.HealthCare_Backend.usermanagement.patient.service.PatientService;

@Tag(name = "Payments", description = "Razorpay payment operations — create order and verify payment signature")
@SecurityRequirement(name = "Bearer Auth")
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

    @Operation(
        summary = "Create Razorpay order",
        description = "Creates a new Razorpay order. Pass amount in rupees (e.g. 500 for ₹500). Internally converts to paise (×100). Returns orderId, amount, currency and Razorpay key"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order created successfully, returns orderId and key"),
        @ApiResponse(responseCode = "500", description = "Razorpay API error")
    })
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> body) throws Exception {
        int amount = (int) body.getOrDefault("amount", 0);
        String currency = (String) body.getOrDefault("currency", "INR");
        log.info("Received create-order request: amount={}, currency={}", amount, currency);
        Map<String, Object> resp = razorpayService.createOrder(amount, currency);
        return ResponseEntity.ok(resp);
    }

    @Operation(
        summary = "Verify Razorpay payment",
        description = "Verifies the Razorpay payment signature after successful payment. On success, marks the appointment billing status as PAID. Required fields: razorpay_order_id, razorpay_payment_id, razorpay_signature, appointmentId"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment verified and appointment marked as paid"),
        @ApiResponse(responseCode = "400", description = "appointmentId missing in request"),
        @ApiResponse(responseCode = "500", description = "Signature verification failed")
    })
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> payload) throws Exception {
        log.info("Received verify request with keys: {}", payload.keySet());

        String appointmentIdStr = payload.get("appointmentId");
        if (appointmentIdStr == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "appointmentId is required"));
        }

        Long appointmentId = Long.valueOf(appointmentIdStr);
        razorpayService.verifySignature(payload);
        patientService.makePayment(appointmentId);

        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
