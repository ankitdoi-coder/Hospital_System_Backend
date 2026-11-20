package com.ankit.HealthCare_Backend.usermanagement.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ankit.HealthCare_Backend.billing.dto.BillingDTO;
import com.ankit.HealthCare_Backend.communication.dto.contactusDto;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;



public interface AdminService {
    Page<DoctorDTO> getAllDoctors(Pageable pageable);
    DoctorDTO approveDoctor(Long id);
    Page<PatientDTO> getAllPatients(Pageable pageable);
    DoctorDTO rejectDoctor(Long id);
    List<BillingDTO> getAllBilling();
    BillingDTO updateBillingStatus(Long id, String status);
    Integer getDailyRevenue();
    Integer getMonthlyRevenue();
    Page<contactusDto> getAllEnquries(Pageable pageable);
}
