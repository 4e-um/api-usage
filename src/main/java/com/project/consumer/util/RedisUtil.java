package com.project.consumer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import com.project.producer.schema.CalculatedLimitSchema;
import com.project.producer.schema.UsageEventSchema;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<List> script =
            new DefaultRedisScript<>(LuaScriptLoader.load("lua/usage_batch.lua"), List.class);

    public List<String> applyUsageBatch(List<UsageEventSchema> events) {
        if (events == null || events.isEmpty()) {
            return List.of();
        }

        List<String> args = new ArrayList<>();
        args.add(String.valueOf(events.size()));

        for (UsageEventSchema e : events) {

            args.add(String.valueOf(e.subscriptionId()));
            args.add(e.eventId());
            args.add(String.valueOf(e.usageBytes()));
            args.add(e.timeStamp());
        }

        Object result = redisTemplate.execute(script, Collections.emptyList(), args.toArray());
        return result == null ? List.of() : (List<String>) result;
    }

    public void writePlanChangeBatch(List<CalculatedLimitSchema> limits) {
        redisTemplate.executePipelined(
                (RedisCallback<Void>)
                        connection -> {
                            for (CalculatedLimitSchema limit : limits) {
                                String ke =
                                        "limit:" + limit.yearMonth() + ":" + limit.subscriptionId();

                                byte[] key = redisTemplate.getStringSerializer().serialize(ke);
                                byte[] value =
                                        redisTemplate
                                                .getStringSerializer()
                                                .serialize(String.valueOf(limit.limit()));

                                connection.set(key, value);
                                connection.expire(key, limit.ttlSec());

                                String unitKey = "plan:unit:" + limit.subscriptionId();
                                byte[] uk = redisTemplate.getStringSerializer().serialize(unitKey);
                                byte[] uv =
                                        redisTemplate.getStringSerializer().serialize(limit.unit());

                                connection.set(uk, uv);
                                connection.expire(uk, limit.ttlSec());
                            }
                            return null;
                        });
    }
}
