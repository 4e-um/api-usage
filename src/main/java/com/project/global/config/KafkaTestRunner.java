package com.project.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTestRunner implements CommandLineRunner {

  private final Producer producer;

  @Override
  public void run(String... args) throws Exception {
    producer.sendUsageMessage("key", "사용량 알림입니다");
  }
}
