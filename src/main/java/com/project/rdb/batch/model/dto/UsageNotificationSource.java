package com.project.rdb.batch.model.dto;

public record UsageNotificationSource(
        Long subId, String period, String unit, long totalUsedBytes, long allotmentAmount) {}
