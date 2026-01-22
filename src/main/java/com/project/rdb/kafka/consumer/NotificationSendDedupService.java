package com.project.rdb.kafka.consumer;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationSendDedupService {

    private final StringRedisTemplate redisTemplate;
    private static final Duration TTL = Duration.ofDays(7);

    public boolean tryAcquire(String eventId) {
        Boolean success =
                redisTemplate.opsForValue().setIfAbsent("notification:event:" + eventId, "1", TTL);
        return Boolean.TRUE.equals(success);
    }
}
