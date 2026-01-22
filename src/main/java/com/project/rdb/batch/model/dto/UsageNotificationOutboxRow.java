package com.project.rdb.batch.model.dto;

public record UsageNotificationOutboxRow(
        Long id,
        Long subId,
        String period,
        String unit,
        int threshold,
        int percent,
        long totalUsedMb,
        long allotmentMb) {}
