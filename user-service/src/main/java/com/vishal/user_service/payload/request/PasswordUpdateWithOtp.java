package com.vishal.user_service.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordUpdateWithOtp {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String otp;

    @NotNull
    @Length(min = 8, max = 16)
    private String newPassword;
}
