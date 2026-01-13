package com.project.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsageConsumer {

    private final Producer producer;

    @KafkaListener(
            id = "usageConsumer",
            topics = "usage_topic",
            groupId = "usage-consumer"
    )
    public void consume(String message) {
        System.out.println("usage consumer received data : " + message);
        producer.sendUsageMessage("key", message);
    }
}
