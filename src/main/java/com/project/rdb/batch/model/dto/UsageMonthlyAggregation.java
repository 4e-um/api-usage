package com.project.rdb.batch.model.dto;

public record UsageMonthlyAggregation(Long subId, String period, long deltaBytes) {}
