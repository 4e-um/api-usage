package com.project.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendNotification(String payload) {
    kafkaTemplate.send("notification_topic", payload);
  }
}
