package com.project.rdb.batch.usagenotification.dto;

import java.time.LocalDateTime;

public record UsageNotificationOutboxRow(
        Long id,
        Long subId,
        String period,
        String planName,
        int threshold,
        double percent,
        long totalUsedMb,
        long allotmentMb,
        String phoneNumber,
        String email,
        LocalDateTime createdAt) {}
