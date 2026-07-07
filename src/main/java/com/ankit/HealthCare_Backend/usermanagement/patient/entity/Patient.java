package com.ankit.HealthCare_Backend.usermanagement.patient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

import com.ankit.HealthCare_Backend.Audits.BaseAuditEntity;
import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends BaseAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true) // A user can only be one patient
    private User user;

    
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @Column(name="profile_picture")
    private String profilePicture;  //stores the url of PFP
}