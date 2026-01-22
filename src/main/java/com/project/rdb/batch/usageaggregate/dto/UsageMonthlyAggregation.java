package com.project.rdb.batch.usageaggregate.dto;

public record UsageMonthlyAggregation(Long subId, String period, long deltaBytes) {}
