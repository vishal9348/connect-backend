package com.vishal.user_service.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record UpdateRolesRequest(
        @NotEmpty Set<@Pattern(regexp = "USER|ADMIN|MODERATOR") String> roles
) {}
