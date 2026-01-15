package com.project.consumer.util;

import com.project.consumer.constant.UsageBatchLuaConstant;
import com.project.producer.schema.CalculatedLimitSchema;
import com.project.producer.schema.UsageEventSchema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

  private final StringRedisTemplate redisTemplate;

  private final DefaultRedisScript<List> script =
      new DefaultRedisScript<>(UsageBatchLuaConstant.LUA, List.class);

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

    Object result = redisTemplate.execute(script, Collections.emptyList(), args.toArray());
    return result == null ? List.of() : (List<String>) result;
  }

  public void writePlanChangeBatch(List<CalculatedLimitSchema> limits) {
    redisTemplate.executePipelined(
        (RedisCallback<Void>)
            connection -> {
              for (CalculatedLimitSchema limit : limits) {
                String key = "limit:" + limit.yyyyMM() + ":" + limit.subscriptionId();

                byte[] k = redisTemplate.getStringSerializer().serialize(key);
                byte[] v =
                    redisTemplate.getStringSerializer().serialize(String.valueOf(limit.limit()));

                connection.set(k, v);
                connection.expire(k, limit.ttlSec());

                String unitKey = "plan:unit:" + limit.subscriptionId();
                byte[] uk = redisTemplate.getStringSerializer().serialize(unitKey);
                byte[] uv = redisTemplate.getStringSerializer().serialize(limit.unit());

                connection.set(uk, uv);
                connection.expire(uk, limit.ttlSec());
              }
              return null;
            });
  }
}
