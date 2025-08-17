package com.vishal.user_service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;


@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    @Bean
    public TopicExchange userEventsExchange() {
        return new TopicExchange("user.events");
    }

    @Bean
    public Queue userEventsQueue() {
        return QueueBuilder.durable("user.events.queue")
                .withArgument("x-dead-letter-exchange", "dlx.user.events")
                .build();
    }

    @Bean
    public Binding userEventsBinding() {
        return BindingBuilder.bind(userEventsQueue())
                .to(userEventsExchange())
                .with("user.*");
    }
}
