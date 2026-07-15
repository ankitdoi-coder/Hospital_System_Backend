package com.ankit.HealthCare_Backend.usermanagement.admin.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.ankit.HealthCare_Backend.billing.dto.BillingDTO;
import com.ankit.HealthCare_Backend.communication.dto.contactusDto;
import com.ankit.HealthCare_Backend.usermanagement.admin.service.AdminService;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Admin", description = "Admin operations — requires ADMIN role JWT token")
@SecurityRequirement(name = "Bearer Auth")
@RestController
@RequestMapping("/api/admin")
@Validated
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Get all doctors", description = "Returns all registered doctors including pending, approved and rejected ones")
    @ApiResponse(responseCode = "200", description = "Doctor list returned")
    @GetMapping("/doctors")
    public ResponseEntity<Page<DoctorDTO>> getAllDoctors(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size
    ) {
        
        return ResponseEntity.ok(adminService.getAllDoctors(PageRequest.of(page, size)));
    }

    @Operation(summary = "Approve a doctor", description = "Approves a doctor account so they can login and accept appointments")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor approved successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @PutMapping("/doctors/{id}/approve")
    public ResponseEntity<DoctorDTO> approveDoctor(
            @Parameter(description = "Doctor ID", required = true, example = "1")
            @Positive(message = "Doctor ID must be positive") @PathVariable Long id) {
        DoctorDTO approvedDoctor = adminService.approveDoctor(id);
        return ResponseEntity.ok(approvedDoctor);
    }

    @Operation(summary = "Reject / revoke a doctor", description = "Rejects or revokes a doctor's approval — they will no longer be able to login")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor rejected successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @PutMapping("/doctors/{id}/reject")
    public ResponseEntity<DoctorDTO> rejectDoctor(
            @Parameter(description = "Doctor ID", required = true, example = "1")
            @Positive(message = "Doctor ID must be positive") @PathVariable Long id) {
        DoctorDTO rejectedDoctor = adminService.rejectDoctor(id);
        return ResponseEntity.ok(rejectedDoctor);
    }

    @Operation(summary = "Get all patients", description = "Returns all registered patients")
    @ApiResponse(responseCode = "200", description = "Patient list returned")
    @GetMapping("/patients")
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllPatients(PageRequest.of(page, size)));
    }

    @Operation(summary = "Get all billing records", description = "Returns all billing records across all appointments")
    @ApiResponse(responseCode = "200", description = "Billing list returned")
    @GetMapping("/billing")
    public ResponseEntity<List<BillingDTO>> getAllBilling() {
        List<BillingDTO> billing = adminService.getAllBilling();
        return ResponseEntity.ok(billing);
    }

    @Operation(summary = "Update billing status", description = "Updates the billing status of a record — e.g. PENDING → PAID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Billing status updated"),
        @ApiResponse(responseCode = "404", description = "Billing record not found")
    })
    @PutMapping("/billing/{id}/status")
    public ResponseEntity<BillingDTO> updateBillingStatus(
            @Parameter(description = "Billing ID", required = true, example = "1")
            @Positive(message = "Billing ID must be positive") @PathVariable Long id,
            @NotBlank(message = "Status cannot be blank") @RequestBody String status) {
        BillingDTO billing = adminService.updateBillingStatus(id, status);
        return ResponseEntity.ok(billing);
    }

    @Operation(summary = "Get daily revenue", description = "Returns total revenue collected today across all paid appointments")
    @ApiResponse(responseCode = "200", description = "Daily revenue amount in rupees")
    @GetMapping("/revenue/daily")
    public ResponseEntity<Integer> getDailyRevenue() {
        Integer revenue = adminService.getDailyRevenue();
        return ResponseEntity.ok(revenue);
    }

    @Operation(summary = "Get monthly revenue", description = "Returns total revenue collected this month across all paid appointments")
    @ApiResponse(responseCode = "200", description = "Monthly revenue amount in rupees")
    @GetMapping("/revenue/monthly")
    public ResponseEntity<Integer> getMonthlyRevenue() {
        Integer revenue = adminService.getMonthlyRevenue();
        return ResponseEntity.ok(revenue);
    }
    @Operation(summary = "Get contactus Enquirey Data", description = "Returns the data for contact us")
    @ApiResponse(responseCode = "200", description = "all data of contact us")
    @GetMapping("/enquries")
    public ResponseEntity<Page<contactusDto>> getEnquries(
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="10") int size
    ) {
        return ResponseEntity.ok(adminService.getAllEnquries(PageRequest.of(page, size)));
    }
    
}
