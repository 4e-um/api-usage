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
        producer.sendNotificationMessage("key", "안녕하세요 테스트 진행중입니다");
    }
}
