package com.vishal.user_service.service;

import java.util.Set;
import java.util.UUID;

public interface IEventPublisher {
    void publishUserRegisteredEvent(UUID userId, String email);
    void publishFollowEvent(UUID followerId, UUID followeeId);
    void publishRolesUpdatedEvent(UUID userId, Set<String> roles);
    void publishProfileUpdatedEvent(UUID userId);
    void publishUserDeactivatedEvent(UUID userId);
    void publishUnfollowEvent(UUID followerId, UUID followeeId);
    void publishPasswordChangedEvent(UUID userId);
}
