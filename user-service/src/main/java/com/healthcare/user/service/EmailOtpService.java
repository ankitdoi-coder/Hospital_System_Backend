package com.healthcare.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class EmailOtpService {

    @Value("${app.otp.expiry-minutes:10}")
    private int otpExpiryMinutes;

    private static class OtpEntry {
        final String otp;
        final Instant expiry;

        OtpEntry(String otp, Instant expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }
    }

    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, Instant> verifiedEmails = new ConcurrentHashMap<>();

    public void sendOtp(String email) {
        String cleanEmail = email.toLowerCase().trim();
        String otp = String.format("%06d", new Random().nextInt(999999));
        Instant expiry = Instant.now().plusSeconds(otpExpiryMinutes * 60L);

        otpStorage.put(cleanEmail, new OtpEntry(otp, expiry));
        verifiedEmails.remove(cleanEmail);

        log.info("=================================================");
        log.info(">>>>> [HEALTHCARE OTP] Code for {}: {} <<<<<", cleanEmail, otp);
        log.info(">>>>> Valid for {} minutes <<<<<", otpExpiryMinutes);
        log.info("=================================================");
    }

    public boolean verifyOtp(String email, String otp) {
        String cleanEmail = email.toLowerCase().trim();
        OtpEntry entry = otpStorage.get(cleanEmail);
        if (entry == null) {
            return false;
        }

        if (Instant.now().isAfter(entry.expiry)) {
            otpStorage.remove(cleanEmail);
            return false;
        }

        if (!entry.otp.equals(otp.trim())) {
            return false;
        }

        // Clean up OTP after successful verification (one-time use)
        otpStorage.remove(cleanEmail);
        verifiedEmails.put(cleanEmail, Instant.now().plusSeconds((otpExpiryMinutes + 15) * 60L));
        return true;
    }

    public boolean isEmailVerified(String email) {
        String cleanEmail = email.toLowerCase().trim();
        Instant expiry = verifiedEmails.get(cleanEmail);
        if (expiry == null) {
            return false;
        }
        if (Instant.now().isAfter(expiry)) {
            verifiedEmails.remove(cleanEmail);
            return false;
        }
        return true;
    }
}
