package com.vishal.user_service.event;

import com.vishal.user_service.payload.request.FollowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishFollowEvent(FollowEvent event) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.follow",
                event
        );
    }

    public void publishUserDeactivatedEvent(UUID userId) {
        rabbitTemplate.convertAndSend(
                "user.events",
                "user.deactivated",
                Map.of("userId", userId)
        );
    }
}
