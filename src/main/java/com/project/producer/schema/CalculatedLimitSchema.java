package com.project.producer.schema;

public record CalculatedLimitSchema(
    long subscriptionId, String yearMonth, long limit, long ttlSec, String unit) {}
