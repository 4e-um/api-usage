package com.project.producer;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.project.global.exception.ApplicationException;
import com.project.global.exception.code.domain.GlobalErrorCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.producer.schema.UsageEventSchema;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendUsageEvent(
            long subscriptionId, long usageBytes, String yearMonth, long ttlSec) {
        UsageEventSchema schema =
                new UsageEventSchema(
                        UUID.randomUUID().toString(),
                        subscriptionId,
                        usageBytes,
                        OffsetDateTime.now().toString(),
                        yearMonth,
                        ttlSec);

        try {
            String value = objectMapper.writeValueAsString(schema);
            String key = String.valueOf(subscriptionId);

            kafkaTemplate.send("usage-data", key, value);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(GlobalErrorCode.JSON_CONVERT_INVALID);
        }
    }
}
