package com.ankit.HealthCare_Backend.usermanagement.user.entity;

import com.ankit.HealthCare_Backend.core.entity.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name ="Email")
    private String email;

    @Column(nullable = false, name = "Password")
    private String password;

    @Column(name = "is_approved",nullable = false)
    private boolean isApproved = false;

    //Many users can be assosiated with one Role
    @ManyToOne(fetch = FetchType.EAGER)         
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;
}
