package com.ankit.HealthCare_Backend.authentication.service;

import com.ankit.HealthCare_Backend.authentication.entity.EmailOtp;
import com.ankit.HealthCare_Backend.authentication.repository.EmailOtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailOtpService {

    private final EmailOtpRepository otpRepo;
    private final JavaMailSender mailSender;

    @Value("${app.otp.expiry-minutes:10}")
    private int otpExpiryMinutes;

    @Transactional
    public void sendOtp(String email) {
        otpRepo.deleteByEmail(email); // clear any previous OTP for this email

        String otp = String.format("%06d", new Random().nextInt(999999));
        otpRepo.save(new EmailOtp(email, otp, otpExpiryMinutes));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify Your Email — HealthCare Portal");
        message.setText(
            "Hello,\n\n" +
            "Thank you for registering with HealthCare Portal.\n\n" +
            "Please use the One-Time Password (OTP) below to verify your email address:\n\n" +
            "    OTP: " + otp + "\n\n" +
            "This OTP is valid for 10 minutes. For your security, do not share this\n" +
            "code with anyone, including HealthCare Portal support staff.\n\n" +
            "If you did not attempt to register, please ignore this email.\n" +
            "Your account will not be created without completing verification.\n\n" +
            "Regards,\n" +
            "The HealthCare Portal Team"
        );
        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {
        return otpRepo.findTopByEmailOrderByExpiryTimeDesc(email)
                .filter(o -> !o.isVerified())
                .filter(o -> o.getOtp().equals(otp))
                .filter(o -> o.getExpiryTime().isAfter(LocalDateTime.now()))
                .map(o -> {
                    o.setVerified(true);
                    otpRepo.save(o);
                    return true;
                })
                .orElse(false);
    }

    public boolean isEmailVerified(String email) {
        return otpRepo.findTopByEmailOrderByExpiryTimeDesc(email)
                .map(EmailOtp::isVerified)
                .orElse(false);
    }
}
