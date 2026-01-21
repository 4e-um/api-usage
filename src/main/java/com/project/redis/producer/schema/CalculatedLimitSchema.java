package com.project.redis.producer.schema;

import com.project.redis.producer.test.PlanUnit;

public record CalculatedLimitSchema(
        long subscriptionId, String yearMonth, long limit, long ttlSec, PlanUnit unit) {}
