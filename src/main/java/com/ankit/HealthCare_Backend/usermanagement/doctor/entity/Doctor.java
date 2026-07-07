package com.ankit.HealthCare_Backend.usermanagement.doctor.entity;

import com.ankit.HealthCare_Backend.Audits.BaseAuditEntity;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends BaseAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true) // A user can only be one doctor
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "specialty", nullable = false)
    private String specialty;

    @Column(name="is_approved",nullable = false)
    private boolean isApproved = false;

    @Column(name = "experience", nullable = false)
    private String experience; // in years

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name="profile_picture")
    private String profilePicture;  //stores the url of PFP


}