package com.project.redis.producer.schema;

public record UsageEventSchema(
        String eventId,
        long subscriptionId,
        long usageBytes,
        String timeStamp,
        String event,
        long ttlSec) {}
