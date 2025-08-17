package com.vishal.user_service.event;

import com.vishal.user_service.payload.request.FollowEvent;
import com.vishal.user_service.service.IEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RabbitMQEventPublisher implements IEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishUserRegisteredEvent(UUID userId, String email) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.registered",
                Map.of(
                        "userId", userId,
                        "email", email,
                        "timestamp", Instant.now()
                )
        );
    }

    @Override
    public void publishFollowEvent(UUID followerId, UUID followeeId) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.follow",
                new FollowEvent(followerId, followeeId)
        );
    }

    @Override
    public void publishUnfollowEvent(UUID followerId, UUID followeeId) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.unfollow",
                Map.of(
                        "followerId", followerId,
                        "followeeId", followeeId,
                        "timestamp", Instant.now()
                )
        );
    }

    @Override
    public void publishRolesUpdatedEvent(UUID userId, Set<String> roles) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.roles.updated",
                Map.of(
                        "userId", userId,
                        "roles", roles,
                        "timestamp", Instant.now()
                )
        );
    }

    @Override
    public void publishProfileUpdatedEvent(UUID userId) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.profile.updated",
                Map.of(
                        "userId", userId,
                        "timestamp", Instant.now()
                )
        );
    }

    @Override
    public void publishUserDeactivatedEvent(UUID userId) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.deactivated",
                Map.of(
                        "userId", userId,
                        "timestamp", Instant.now()
                )
        );
    }

    @Override
    public void publishPasswordChangedEvent(UUID userId) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.password.changed",
                Map.of(
                        "userId", userId,
                        "timestamp", Instant.now()
                )
        );
    }
}
