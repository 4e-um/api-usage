package com.project.rdb.kafka.consumer;

import com.project.rdb.batch.notificationsend.dto.NotificationMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSendConsumer {

    private final NotificationSendDedupService dedupService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "notification-usage",
            containerFactory = "kafkaListenerContainerFactory")
    @Profile("notification-worker")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("🔥 CONSUME START offset={}, value={}", record.offset(), record.value());

        try {
            NotificationMessage event =
                    objectMapper.readValue(record.value(), NotificationMessage.class);

            String eventId = event.eventId().toString();

            if (!dedupService.tryAcquire(eventId)) {
                log.info("[SKIP] duplicated eventId={}", eventId);
                ack.acknowledge();
                return;
            }

            log.info("[SUCCESS] finished eventId={}", eventId);

            ack.acknowledge();
        } catch (Exception e) {
            log.error("[CONSUME FAIL]", e);
            ack.acknowledge(); // 지금 구조상 스킵이 맞음
        }
    }
}
