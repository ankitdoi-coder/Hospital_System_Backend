package com.healthcare.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String specialization;
    private String department;
    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
    private boolean isApproved;
}
