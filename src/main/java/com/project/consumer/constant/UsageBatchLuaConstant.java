package com.project.consumer.constant;

public final class UsageBatchLuaConstant {

  private UsageBatchLuaConstant() {}

  public static final String LUA =
      """
        -- ARGV:
        -- [1] N 이후 반복 N개:
        -- timeKey, subId, eventId, bytes, ts, ttlSec

        local out = {}
        local n = tonumber(ARGV[1])
        local idx = 2


        for i = 1, n do
            local subId   = ARGV[idx]; idx = idx + 1
            local eventId = ARGV[idx]; idx = idx + 1
            local bytes   = tonumber(ARGV[idx]); idx = idx + 1
            local ts      = ARGV[idx]; idx = idx + 1

            local monthKeyTime = string.sub(ts, 1, 7):gsub('-', '')
            local limitKey     = 'limit:' .. monthKeyTime .. ':' .. subId

            local unit = redis.call('GET', 'plan:unit:' .. subId) or 'MONTH'

            local usageKeyTime
            local thKeyTime
            local ttlSec

            if unit == 'DAY' then
                usageKeyTime = string.sub(ts, 1, 10):gsub('-', '')
                thKeyTime    = usageKeyTime
                ttlSec = 86400 * 2
            else
                usageKeyTime = monthKeyTime
                thKeyTime    = monthKeyTime
                ttlSec = redis.call('TTL', limitKey)
                if ttlSec < 0 then ttlSec = 86400 * 35 end
            end

            local usageKey     = 'usage:' .. usageKeyTime .. ':' .. subId
            local processedKey = 'processed:usage:' .. usageKeyTime .. ':' .. subId
            local thKey        = 'th:' .. thKeyTime .. ':' .. subId

            -- dedup: 이미 처리된 이벤트면 아래 로직을 아예 실행 안 함
            if redis.call('SISMEMBER', processedKey, eventId) == 0 then
                redis.call('SADD', processedKey, eventId)
                if redis.call('TTL', processedKey) < 0 then
                    redis.call('EXPIRE', processedKey, ttlSec)
                end

                local newTotal = redis.call('INCRBY', usageKey, bytes)
                if redis.call('TTL', usageKey) < 0 then
                    redis.call('EXPIRE', usageKey, ttlSec)
                end

                local limit = tonumber(redis.call('GET', limitKey) or '0')
                if limit > 0 then
                    local percent = math.floor((newTotal * 100) / limit)

                    local prev = tonumber(redis.call('GET', thKey) or '0')
                    local next = prev

                    if percent >= 100 and prev < 100 then next = 100
                    elseif percent >= 80 and prev < 80 then next = 80
                    elseif percent >= 50 and prev < 50 then next = 50
                    end

                    if next ~= prev then
                        redis.call('SET', thKey, tostring(next))
                        if redis.call('TTL', thKey) < 0 then
                            redis.call('EXPIRE', thKey, ttlSec)
                        end

                        table.insert(out,
                            thKeyTime .. '|' .. subId .. '|' .. next .. '|' .. percent .. '|' ..
                            newTotal .. '|' .. limit .. '|' .. eventId .. '|' .. ts)
                    end
                end
            end
        end
        return out
    """;
}
