package com.project.producer.schema;

public record ExceededNotificationSchema(
        long subscriptionId,
        long newTotal,
        long limit,
        String eventId,
        String timeStamp
) {
}
