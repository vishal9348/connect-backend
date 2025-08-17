package com.vishal.user_service.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordUpdateRequest {

    @NotNull
    private String oldPassword;

    @NotNull
    @Length(min = 8, max = 16)
    private String newPassword;
}
