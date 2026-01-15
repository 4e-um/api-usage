package com.project.producer.schema;

public record UsageDlqEventSchema(
        String originKey,
        String originalValue,
        String error,
        String failedAt
) {
}
