package com.ankit.HealthCare_Backend.authentication.service;

import com.ankit.HealthCare_Backend.authentication.entity.EmailOtp;
import com.ankit.HealthCare_Backend.authentication.repository.EmailOtpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailOtpService {

    private final EmailOtpRepository otpRepo;
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.otp.expiry-minutes:10}")
    private int otpExpiryMinutes;

    private String otpKey(String email) {
        return "otp:" + email;
    }

    private String verifiedKey(String email) {
        return "otp:verified:" + email;
    }

    @Transactional
    public void sendOtp(String email) {

        String otp = String.format("%06d", new Random().nextInt(999999));

        redisTemplate.opsForValue().set(otpKey(email), otp, Duration.ofMinutes(otpExpiryMinutes));
        redisTemplate.delete(verifiedKey(email));

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
                        "Team HealthCare Copilot");
        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {
        String stored = redisTemplate.opsForValue().get(otpKey(email));
        if (stored == null || !stored.equals(otp)) {
            return false;
        }
        redisTemplate.delete(otpKey(email)); // one-time use — consumed on successful verify
        // Give the user extra time (otpExpiryMinutes + 15) to complete the registration
        // form
        // after verifying, without needing to re-verify the OTP itself.
        redisTemplate.opsForValue().set(verifiedKey(email), "true", Duration.ofMinutes(otpExpiryMinutes + 15));
        return true;
    }

    public boolean isEmailVerified(String email) {
        return "true".equals(redisTemplate.opsForValue().get(verifiedKey(email)));
    }
}
