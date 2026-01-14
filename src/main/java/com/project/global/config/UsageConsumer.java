package com.project.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsageConsumer {

  private final Producer producer;

  @KafkaListener(id = "usageConsumer", topics = "usage_topic", groupId = "usage-consumer")
  public void consume(String message) {
    System.out.println("usage consumer received data : " + message);
    producer.sendNotificationMessage("key", "데이터 사용량 누적 임계치 경고 알림입니다");
  }
}
