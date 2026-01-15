package com.project.consumer.util;

import com.project.consumer.constant.UsageBatchLuaConstant;
import com.project.producer.schema.CalculatedLimitSchema;
import com.project.producer.schema.UsageEventSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<List> script
            = new DefaultRedisScript<>(UsageBatchLuaConstant.LUA, List.class);

    public List<String> applyUsageBatch(List<UsageEventSchema> events) {
        if (events == null || events.isEmpty()) return List.of();

        List<String> args = new ArrayList<>();
        args.add(String.valueOf(events.size()));

        for (UsageEventSchema e : events) {

            args.add(String.valueOf(e.subscriptionId()));
            args.add(e.eventId());
            args.add(String.valueOf(e.usageBytes()));
            args.add(e.timeStamp());
        }

        Object result = redisTemplate.execute(
                script,
                Collections.emptyList(),
                args.toArray()
        );
        return result == null ? List.of() : (List<String>) result;

    }
}
