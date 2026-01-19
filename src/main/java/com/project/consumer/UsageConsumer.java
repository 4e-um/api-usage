package com.project.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.consumer.util.RedisUtil;
import com.project.global.exception.ApplicationException;
import com.project.global.exception.code.domain.GlobalErrorCode;
import com.project.producer.NotificationProducer;
import com.project.producer.schema.UsageEventSchema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsageConsumer {

    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;
    private final NotificationProducer notificationProducer;

    @KafkaListener(
            id = "usage-batch-consumer",
            topics = "usage-data",
            groupId = "usage-consumer",
            containerFactory = "batchKafkaListenerContainerFactory")
    public void consume(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {

        if (records == null || records.isEmpty()) {
            ack.acknowledge();
            return;
        }

        List<UsageEventSchema> events = new ArrayList<>(records.size());

        try {

            for (ConsumerRecord<String, String> rec : records) {
                UsageEventSchema schema =
                        objectMapper.readValue(rec.value(), UsageEventSchema.class);
                events.add(schema);
            }

            List<String> notifications = redisUtil.applyUsageBatch(events);

            // 임계치 넘은 것만 notification_topic으로 발행
            for (String notification : notifications) {
                notificationProducer.sendNotification(notification);
            }

            // 성공한 배치 ack
            ack.acknowledge();
        } catch (Exception e) {
            log.error("usage batch failed", e);
            // 여기서 ack 안 하면 같은 배치가 재시도됨 (at-least-once)
            throw new ApplicationException(GlobalErrorCode.NOTIFICATION_EVENT_PRODUCE_INVALID);
        }
    }
}
