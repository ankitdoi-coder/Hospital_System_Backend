package com.ankit.HealthCare_Backend.usermanagement.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ankit.HealthCare_Backend.billing.dto.BillingDTO;
import com.ankit.HealthCare_Backend.billing.entity.Billing;
import com.ankit.HealthCare_Backend.billing.repository.BillingRepository;
import com.ankit.HealthCare_Backend.core.enums.BillingStatus;
import com.ankit.HealthCare_Backend.usermanagement.doctor.dto.DoctorDTO;
import com.ankit.HealthCare_Backend.usermanagement.doctor.entity.Doctor;
import com.ankit.HealthCare_Backend.usermanagement.doctor.repository.DoctorRepository;
import com.ankit.HealthCare_Backend.usermanagement.patient.dto.PatientDTO;
import com.ankit.HealthCare_Backend.usermanagement.patient.entity.Patient;
import com.ankit.HealthCare_Backend.usermanagement.patient.repository.PatientRepository;

import java.time.LocalDate;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private PatientRepository patientRepo;
    
    @Autowired
    private BillingRepository billingRepo;

    // get the list of the doctors
    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream()
                .map(appointment -> convertToDoctorDto(appointment))
                .collect(Collectors.toList());
    }

    // approve Doctor
    @Override
    @Transactional // Make sure the operation is transactional
    public DoctorDTO approveDoctor(Long id) {
        // Find the doctor, or throw an exception if not found
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        // Update the doctor's status
        doctor.setApproved(true);

        // Save the updated doctor (optional if inside a @Transactional method, but good
        // practice)
        Doctor savedDoctor = doctorRepo.save(doctor);

        // Convert the updated and saved doctor to a DTO and return it
        return convertToDoctorDto(savedDoctor);
    }

    // get all the patient
    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepo.findAll();
        return patients.stream()
                .map(appointment -> convertToPatientDto(appointment))
                .collect(Collectors.toList());
    }

    // helper method
    DoctorDTO convertToDoctorDto(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setApproved(doctor.isApproved());
        return dto;
    }

    PatientDTO convertToPatientDto(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setUserId(patient.getUser().getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setContactNumber(patient.getContactNumber());
        dto.setDob(patient.getDob());
        return dto;
    }

    // In AdminServiceImpl
    @Override
    @Transactional
    public DoctorDTO rejectDoctor(Long id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        doctor.setApproved(false);
        Doctor savedDoctor = doctorRepo.save(doctor);

        return convertToDoctorDto(savedDoctor);
    }

    @Override
    public List<BillingDTO> getAllBilling() {
        List<Billing> billings = billingRepo.findAllWithDetails();
        return billings.stream()
                .map(this::convertToBillingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BillingDTO updateBillingStatus(Long id, String status) {
        String cleanStatus = status.replace("\"", "").trim();
        BillingStatus billingStatus = BillingStatus.valueOf(cleanStatus);
        Billing billing = billingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing not found with id: " + id));
        
        billing.setBilling_status(billingStatus);
        Billing savedBilling = billingRepo.save(billing);
        return convertToBillingDto(savedBilling);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getDailyRevenue() {
        LocalDate today = LocalDate.now();
        Integer revenue = billingRepo.calculateDailyRevenue(BillingStatus.PAID, today);
        return revenue != null ? revenue : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMonthlyRevenue() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        Integer revenue = billingRepo.calculateMonthlyRevenue(BillingStatus.PAID, startOfMonth, now);
        return revenue != null ? revenue : 0;
    }

    private BillingDTO convertToBillingDto(Billing billing) {
        BillingDTO dto = new BillingDTO();
        dto.setId(billing.getId());
        dto.setAppointmentId(billing.getAppointment_id().getId());
        dto.setAmount(billing.getAmount());
        dto.setBillingStatus(billing.getBilling_status());
        return dto;
    }
}