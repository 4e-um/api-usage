package com.project.redis.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.global.exception.ApplicationException;
import com.project.global.exception.code.domain.GlobalErrorCode;
import com.project.redis.consumer.util.PlanChangeUtil;
import com.project.redis.consumer.util.RedisUtil;
import com.project.redis.producer.schema.CalculatedLimitSchema;
import com.project.redis.producer.schema.PlanChangeSchema;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlanChangeConsumer {

    private final ObjectMapper objectMapper;
    private final PlanChangeUtil planChangeUtil;
    private final RedisUtil redisUtil;

    @KafkaListener(
            id = "plan-change-consumer",
            topics = "change_plan",
            groupId = "plan-change-consumer",
            containerFactory = "kafkaListenerContainerFactory",
            autoStartup = "false")
    public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        if (records == null || records.isEmpty()) {
            ack.acknowledge();
            return;
        }
        List<PlanChangeSchema> events = new ArrayList<>(records.size());
        try {
            for (ConsumerRecord<String, String> rec : records) {
                events.add(objectMapper.readValue(rec.value(), PlanChangeSchema.class));
            }

            List<CalculatedLimitSchema> limits = planChangeUtil.calculate(events);
            redisUtil.writePlanChangeBatch(limits);

            ack.acknowledge();
        } catch (Exception e) {
            throw new ApplicationException(GlobalErrorCode.PLAN_CHANGE_EVENT_PRODUCE_INVALID);
        }
    }
}
