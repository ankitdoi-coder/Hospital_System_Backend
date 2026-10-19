package com.healthcare.user.service;

import com.healthcare.user.dto.DoctorResponseDTO;
import com.healthcare.user.dto.PatientResponseDTO;

import java.util.List;

public interface UserService {
    PatientResponseDTO getPatientById(Long id);
    DoctorResponseDTO getDoctorById(Long id);
    List<DoctorResponseDTO> getAllDoctors();
    List<PatientResponseDTO> getAllPatients();
}
