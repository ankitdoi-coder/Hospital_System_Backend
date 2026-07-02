package com.ankit.HealthCare_Backend.communication.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Contact Us form submission")
public class ContactUs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Record ID (auto-generated)", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Sender's full name", example = "John Doe")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Sender's email address", example = "john@gmail.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Subject of the message", example = "Issue with appointment booking")
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Message content", example = "I am unable to book an appointment with Dr. Smith...")
    private String message;

    @Column(name = "created_at")
    @Schema(description = "Submission timestamp (auto-set)", example = "2025-08-20T10:30:00")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
