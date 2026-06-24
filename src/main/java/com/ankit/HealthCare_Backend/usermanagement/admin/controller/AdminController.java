package com.ankit.HealthCare_Backend.usermanagement.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.HealthCare_Backend.billing.dto.BillingDTO;
import com.ankit.HealthCare_Backend.usermanagement.admin.service.AdminService;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    // doctors list
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        // 1. Give the list a variable name, like "doctors"
        List<DoctorDTO> doctors = adminService.getAllDoctors();

        // 2. Return the list inside a ResponseEntity
        return ResponseEntity.ok(doctors);
    }

    // to approve
    @PutMapping("/doctors/{id}/approve")
    public ResponseEntity<DoctorDTO> approveDoctor(@Positive(message = "Doctor ID must be positive") @PathVariable Long id) {
        DoctorDTO approvedDoctor = adminService.approveDoctor(id);

        return ResponseEntity.ok(approvedDoctor);
    }
// to reject/revoke doctor

    @PutMapping("/doctors/{id}/reject")
    public ResponseEntity<DoctorDTO> rejectDoctor(@Positive(message = "Doctor ID must be positive") @PathVariable Long id) {
        DoctorDTO rejectedDoctor = adminService.rejectDoctor(id);
        return ResponseEntity.ok(rejectedDoctor);
    }

    // patient list
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    //Get all billing records
    @GetMapping("/billing")
    public ResponseEntity<List<BillingDTO>> getAllBilling(){
        List<BillingDTO> billing = adminService.getAllBilling();
        return ResponseEntity.ok(billing);
    }

    //Update billing status
    @PutMapping("/billing/{id}/status")
    public ResponseEntity<BillingDTO> updateBillingStatus(
            @Positive(message = "Billing ID must be positive") @PathVariable Long id,
            @NotBlank(message = "Status cannot be blank") @RequestBody String status) {
        BillingDTO billing = adminService.updateBillingStatus(id, status);
        return ResponseEntity.ok(billing);
    }

    //Get revenue stats
    @GetMapping("/revenue/daily")
    public ResponseEntity<Integer> getDailyRevenue(){
        Integer revenue = adminService.getDailyRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<Integer> getMonthlyRevenue(){
        Integer revenue = adminService.getMonthlyRevenue();
        return ResponseEntity.ok(revenue);
    }
}
