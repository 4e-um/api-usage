package com.project.global.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(
            id = "notificationConsumer",
            topics = "notification_topic",
            groupId = "notification-consumer-test-1"
    )
    public void consume(String message) {
        System.out.println("notification consumer received data : " + message);
    }
}
