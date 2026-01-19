package com.project.producer;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.global.exception.ApplicationException;
import com.project.global.exception.code.domain.GlobalErrorCode;
import com.project.producer.schema.PlanChangeSchema;
import com.project.producer.test.PlanUnit;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlanChangeProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendPlanChangeEvent(
            long subscriptionId,
            PlanUnit unit, // MONTH / DAILY / UNLIMITED
            long allowanceAmount,
            OffsetDateTime changedAt,
            String email,
            String phone) {
        PlanChangeSchema event =
                new PlanChangeSchema(
                        UUID.randomUUID().toString(),
                        subscriptionId,
                        unit,
                        allowanceAmount,
                        changedAt,
                        email,
                        phone);

        try {
            String value = objectMapper.writeValueAsString(event);
            String key = String.valueOf(subscriptionId);

            kafkaTemplate.send("change_plan", key, value);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(GlobalErrorCode.PLAN_CHANGE_EVENT_PRODUCE_INVALID);
        }
    }
}
