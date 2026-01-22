package com.project.rdb.batch.model.dto;

public record UsageNotificationSource(
        Long subId, String period, String unit, String planName, long totalUsedBytes, long allotmentAmount) {}
