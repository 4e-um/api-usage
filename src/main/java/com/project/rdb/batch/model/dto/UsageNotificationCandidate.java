package com.project.rdb.batch.model.dto;

public record UsageNotificationCandidate(
        Long subId,
        String period,
        String unit,
        String planName,
        int threshold,
        double percent,
        long totalUsedMb,
        long allotmentMb) {}
