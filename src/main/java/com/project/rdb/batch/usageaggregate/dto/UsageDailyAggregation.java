package com.project.rdb.batch.usageaggregate.dto;

public record UsageDailyAggregation(Long subId, String usageDate, long deltaBytes) {}
