package com.ankit.HealthCare_Backend.usermanagement.admin.entity;

import com.ankit.HealthCare_Backend.Audits.BaseAuditEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "is_active")
    private boolean isActive = true;

    @Column(nullable = false, name = "name")
    private String name;
}
