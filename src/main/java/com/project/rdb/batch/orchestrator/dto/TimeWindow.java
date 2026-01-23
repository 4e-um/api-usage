package com.project.rdb.batch.orchestrator.dto;

import java.time.LocalDateTime;

public record TimeWindow(LocalDateTime from, LocalDateTime to) {}
