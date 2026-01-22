package com.project.rdb.batch.model.dto;

public record UsageDailyAggregation(Long subId, String usageDate, long deltaBytes) {}
