package com.ankit.HealthCare_Backend.usermanagement.patient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

import com.ankit.HealthCare_Backend.usermanagement.user.entity.User;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true) // A user can only be one patient
    private User user;

    // ✅ Changed column name to snake_case
    @Column(name = "first_name", nullable = false)
    private String firstName;

    // ✅ Changed column name to snake_case
    @Column(name = "last_name")
    private String lastName;

    // ✅ Changed type to LocalDate and column name to snake_case
    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    // ✅ Changed column name to snake_case
    @Column(name = "contact_number", nullable = false)
    private Long contactNumber;
}