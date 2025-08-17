package com.vishal.user_service.payload.request;

import java.time.Instant;
import java.util.UUID;

public record FollowEvent(
        UUID followerId,
        UUID followeeId,
        Instant timestamp
) {
    public FollowEvent(UUID followerId, UUID followeeId) {
        this(followerId, followeeId, Instant.now());
    }
}
