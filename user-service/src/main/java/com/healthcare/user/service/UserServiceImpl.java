package com.healthcare.user.service;

import com.healthcare.user.dto.DoctorResponseDTO;
import com.healthcare.user.dto.PatientResponseDTO;
import com.healthcare.user.entity.Doctor;
import com.healthcare.user.entity.Patient;
import com.healthcare.user.repository.DoctorRepository;
import com.healthcare.user.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(Long id) {
        log.info("Fetching patient with ID: {}", id);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));

        return PatientResponseDTO.builder()
                .id(patient.getId())
                .userId(patient.getUser().getId())
                .fullName(patient.getUser().getFullName())
                .email(patient.getUser().getEmail())
                .phone(patient.getUser().getPhone())
                .bloodGroup(patient.getBloodGroup())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(Long id) {
        log.info("Fetching doctor with ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .fullName(doctor.getUser().getFullName())
                .email(doctor.getUser().getEmail())
                .specialization(doctor.getSpecialization())
                .department(doctor.getDepartment())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .isApproved(doctor.isApproved())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doc -> DoctorResponseDTO.builder()
                        .id(doc.getId())
                        .userId(doc.getUser().getId())
                        .fullName(doc.getUser().getFullName())
                        .email(doc.getUser().getEmail())
                        .specialization(doc.getSpecialization())
                        .department(doc.getDepartment())
                        .qualification(doc.getQualification())
                        .experienceYears(doc.getExperienceYears())
                        .consultationFee(doc.getConsultationFee())
                        .isApproved(doc.isApproved())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(p -> PatientResponseDTO.builder()
                        .id(p.getId())
                        .userId(p.getUser().getId())
                        .fullName(p.getUser().getFullName())
                        .email(p.getUser().getEmail())
                        .phone(p.getUser().getPhone())
                        .bloodGroup(p.getBloodGroup())
                        .gender(p.getGender())
                        .address(p.getAddress())
                        .build())
                .collect(Collectors.toList());
    }
}
