package com.project.rdb.batch.model.dto;

import java.time.LocalDateTime;

public record UsageLogRow(Long subId, Long usedBytes, LocalDateTime eventTime) {}
