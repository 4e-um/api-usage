package com.project.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsageDlqProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String key, String jsonValue) {
        kafkaTemplate.send("usage_dlq_topic", key, jsonValue);
    }
}
