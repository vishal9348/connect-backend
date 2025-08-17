package com.vishal.user_service.payload.response;

import com.vishal.user_service.model.Users;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String fullName,
        String bio,
        String profileImageUrl,
        boolean verified,
        long totalFollowers,
        long totalFollowing,
        Set<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(Users user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getBio(),
                user.getProfileImageUrl(),
                user.isVerified(),
                user.getTotalFollowers(),
                user.getTotalFollowing(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
