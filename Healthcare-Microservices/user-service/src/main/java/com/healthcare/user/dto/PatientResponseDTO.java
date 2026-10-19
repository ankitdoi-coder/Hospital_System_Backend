package com.healthcare.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String bloodGroup;
    private String gender;
    private String address;
}
