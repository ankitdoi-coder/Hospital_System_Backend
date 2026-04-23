package com.ankit.HealthCare_Backend.usermanagement.admin.service;

import java.util.List;


import com.ankit.HealthCare_Backend.billing.dto.BillingDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;



public interface AdminService {
    List<DoctorDTO> getAllDoctors();
    DoctorDTO approveDoctor(Long id);
    List<PatientDTO> getAllPatients();
    DoctorDTO rejectDoctor(Long id);
    List<BillingDTO> getAllBilling();
    BillingDTO updateBillingStatus(Long id, String status);
    Integer getDailyRevenue();
    Integer getMonthlyRevenue();
}
