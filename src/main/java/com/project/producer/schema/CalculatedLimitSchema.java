package com.project.producer.schema;

import com.project.producer.test.PlanUnit;

public record CalculatedLimitSchema(
        long subscriptionId, String yearMonth, long limit, long ttlSec, PlanUnit unit) {}
