package com.vishal.user_service.service.impl;

import com.vishal.user_service.service.IOtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements IOtpService {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRATION_SECONDS = 5 * 60; // 5 minutes

    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();

    private final JavaMailSender mailSender;

    @Override
    public void generateAndSendOtp(String email) {
        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusSeconds(OTP_EXPIRATION_SECONDS);

        otpStore.put(email, new OtpEntry(otp, expiresAt));
        log.info("Generated OTP for {}: {}", email, otp);

        sendEmail(email, otp);
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        OtpEntry entry = otpStore.get(email);
        if (entry == null || Instant.now().isAfter(entry.expiresAt)) {
            otpStore.remove(email);
            return false;
        }
        return entry.otp.equals(otp);
    }

    @Override
    public void invalidateOtp(String email) {
        otpStore.remove(email);
    }

    // Helper methods

    private String generateOtp() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            sb.append(random.nextInt(10)); // digits 0–9
        }
        return sb.toString();
    }

    private void sendEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Your OTP Code");
            helper.setText("<p>Your OTP is: <b>" + otp + "</b>. It will expire in 5 minutes.</p>", true);

            mailSender.send(message);
        } catch (MessagingException | jakarta.mail.MessagingException e) {
            log.error("Failed to send OTP email to {}", to, e);
        }
    }

    private static class OtpEntry {
        String otp;
        Instant expiresAt;

        OtpEntry(String otp, Instant expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }
}
