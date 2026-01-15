package com.project.producer.schema;

import java.time.OffsetDateTime;

public record PlanChangeSchema(
        String eventId,              // 멱등성/추적용
        long subscriptionId,         // 회선 ID
        String unit,               // MONTHLY | DAILY | UNLIMITED
        long allowanceAmount,        // 월 제공량 or 일 제공량 (bytes 기준)
        OffsetDateTime changedAt,     // 요금제 변경 시점
        String email,                // 사용자 이메일
        String phone
) {
}
