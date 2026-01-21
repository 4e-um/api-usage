package com.project.rdb.batch.model.entity;

public enum OutboxStatus {
    PENDING,
    SENT,
    PROCESSING,
    FAILED
}
