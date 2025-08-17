package com.vishal.user_service.payload.response;

public record FollowResponse(
        String username,
        long totalFollowers,
        long totalFollowing,
        boolean isFollowing
) {}