package com.project.rdb.batch.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usage_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsageSummary {

    @EmbeddedId private UsageSummaryId id;

    @Column(nullable = false)
    private long totalUsedBytes;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
