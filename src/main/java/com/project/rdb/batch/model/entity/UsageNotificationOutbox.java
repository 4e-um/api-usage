package com.project.rdb.batch.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usage_notification_outbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsageNotificationOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sub_id", nullable = false)
    private Long subId;

    @Column(nullable = false, length = 8)
    private String period;

    @Column(nullable = false, length = 10)
    private String unit;

    @Column(nullable = false)
    private int threshold;

    @Column(nullable = false)
    private double percent;

    @Column(name = "total_used_mb", nullable = false)
    private Long totalUsedMb;

    @Column(name = "allotment_mb", nullable = false)
    private Long allotmentMb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OutboxStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "failure_reason")
    private String failureReason;
}
