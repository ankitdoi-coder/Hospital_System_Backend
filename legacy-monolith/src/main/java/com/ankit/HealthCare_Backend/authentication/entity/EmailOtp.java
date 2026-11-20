package com.ankit.HealthCare_Backend.authentication.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.ankit.HealthCare_Backend.Audits.BaseAuditEntity;

@Entity
@Table(name = "email_otps")
@Data
@NoArgsConstructor
public class EmailOtp extends BaseAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false)
    private boolean verified = false;

    public EmailOtp(String email, String otp, int expiryMinutes) {
        this.email = email;
        this.otp = otp;
        this.expiryTime = LocalDateTime.now().plusMinutes(expiryMinutes);
    }
}
