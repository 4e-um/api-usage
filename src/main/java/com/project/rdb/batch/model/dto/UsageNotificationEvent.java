package com.project.rdb.batch.model.dto;

import java.util.UUID;

public record UsageNotificationEvent(
        UUID eventId,
        Long id,
        Long subId,
        String period,
        String unit,
        int threshold,
        int percent,
        long totalUsedMb,
        long allotmentMb) {}
