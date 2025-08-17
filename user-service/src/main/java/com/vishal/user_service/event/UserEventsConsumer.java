package com.vishal.user_service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventsConsumer {

//    private final NotificationService notificationService;
//    private final AnalyticsService analyticsService;
//
//    @RabbitListener(queues = "user.events.queue")
//    public void handleUserEvent(Message message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
//        Map<String, Object> payload = convertMessage(message);
//
//        switch (routingKey) {
//            case "user.registered":
//                handleRegistration(payload);
//                break;
//            case "user.follow":
//                handleFollowEvent(payload);
//                break;
//            // ... other cases
//        }
//    }
//
//    private void handleRegistration(Map<String, Object> payload) {
//        UUID userId = (UUID) payload.get("userId");
//        String email = (String) payload.get("email");
//        notificationService.sendWelcomeEmail(userId, email);
//        analyticsService.trackUserRegistration(userId);
//    }
}
