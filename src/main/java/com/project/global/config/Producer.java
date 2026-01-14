package com.project.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendUsageMessage(String key, String value) {
    kafkaTemplate.send("usage_topic", key, value);
  }

  public void sendNotificationMessage(String key, String value) {
    kafkaTemplate.send("notification_topic", key, value);
  }
}
