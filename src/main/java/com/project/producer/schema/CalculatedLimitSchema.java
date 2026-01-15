package com.project.producer.schema;

public record CalculatedLimitSchema(
        long subscriptionId,
        String yyyyMM,
        long limit,
        long ttlSec,
        String unit
) {
}
