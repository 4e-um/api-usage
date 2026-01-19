package com.project.consumer.util;

import static com.project.consumer.util.UsageTimeUtil.toYearMonth;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.project.producer.test.PlanUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.project.producer.schema.CalculatedLimitSchema;
import com.project.producer.schema.PlanChangeSchema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanChangeUtil {

    private final StringRedisTemplate redisTemplate;

    public List<CalculatedLimitSchema> calculate(List<PlanChangeSchema> events) {

        List<CalculatedLimitSchema> results = new ArrayList<>();

        String yearMonth;
        long finalLimit;
        String processedKey;
        Long added;

        for (PlanChangeSchema event : events) {
            yearMonth = toYearMonth(event.changedAt().toString());

            processedKey = "processed:plan:" + yearMonth + ":" + event.subscriptionId();
            added = redisTemplate.opsForSet().add(processedKey, event.eventId());

            redisTemplate.expire(
                    processedKey,
                    Duration.ofSeconds(
                            UsageTimeUtil.ttlToNextMonthWithBufferSec(
                                    event.changedAt().toString(), 2)));

            if (added == null || added == 0L) {
                // 이미 처리된 요금제 변경 이벤트 → skip
                continue;
            }

            String unitKey = "plan:unit:" + event.subscriptionId();
            String prevUnit = redisTemplate.opsForValue().get(unitKey);

            if (event.unit().equals(PlanUnit.ULTIMATE)) {
                finalLimit = -1L;
            } else if (event.unit().equals(PlanUnit.DAY)) {
                finalLimit = event.allowanceAmount();
            } else {
                finalLimit = getMonthFinalLimit(yearMonth, event, prevUnit);
            }

            long ttlSec =
                    UsageTimeUtil.ttlToNextMonthWithBufferSec(event.changedAt().toString(), 2);

            results.add(
                    new CalculatedLimitSchema(
                            event.subscriptionId(), yearMonth, finalLimit, ttlSec, event.unit()));
        }

        return results;
    }

    private long getPreviousLimit(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value == null ? 0L : Long.parseLong(value);
    }

    // 사용자가 월 기준 중도에 요금제를 변경했을 경우 사용 가능 데이터 집계 로직
    private long getMonthFinalLimit(String yearMonth, PlanChangeSchema event, String prevUnit) {
        OffsetDateTime kstTime =
                event.changedAt().atZoneSameInstant(ZoneId.of("Asia/Seoul")).toOffsetDateTime();

        YearMonth month = YearMonth.from(kstTime);
        int totalDays = month.lengthOfMonth();
        int changeDay = kstTime.getDayOfMonth();

        int daysBefore = changeDay - 1;
        int daysAfter = totalDays - daysBefore;

        // 만약 전 요금제가 무제한 or DAY 요금제 였다면 새로 추가된 요금제의 데이터 양만 계산
        if (!"MONTH".equals(prevUnit)) {
            return event.allowanceAmount() * daysAfter / totalDays;
        }

        String limitKey = "limit:" + yearMonth + ":" + event.subscriptionId();

        long prevLimit = getPreviousLimit(limitKey);

        long allowanceBefore = prevLimit * daysBefore / totalDays;
        long allowanceAfter = event.allowanceAmount() * daysAfter / totalDays;

        return allowanceBefore + allowanceAfter;
    }
}
