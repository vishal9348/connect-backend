package com.vishal.user_service.service;

public interface IOtpService {

    void generateAndSendOtp(String email);
    boolean validateOtp(String email, String otp);
    void invalidateOtp(String email);
}
