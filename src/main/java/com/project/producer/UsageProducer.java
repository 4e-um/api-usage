package com.project.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.producer.schema.UsageEventSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendUsageEvent(long subscriptionId, long usageBytes, String yyyyMM, long ttlSec) {
        UsageEventSchema schema = new UsageEventSchema(
                UUID.randomUUID().toString(),
                subscriptionId,
                usageBytes,
                OffsetDateTime.now().toString(),
                yyyyMM,
                ttlSec
        );

        try {
            String value = objectMapper.writeValueAsString(schema);
            String key = String.valueOf(subscriptionId);

            kafkaTemplate.send("usage-data", key, value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
