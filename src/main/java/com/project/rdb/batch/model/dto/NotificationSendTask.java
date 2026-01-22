package com.project.rdb.batch.model.dto;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.support.SendResult;

public record NotificationSendTask(
        NotificationMessage event, CompletableFuture<SendResult<String, String>> future) {}
