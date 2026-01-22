package com.project.rdb.batch.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "usage_log")
@Getter
@SuperBuilder
@NoArgsConstructor
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, length = 64)
    private String eventId;

    @Column(name = "sub_id", nullable = false)
    private Long subId;

    @Column(name = "used_bytes", nullable = false)
    private Long usedBytes;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
