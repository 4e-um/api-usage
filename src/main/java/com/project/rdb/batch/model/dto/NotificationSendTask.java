package com.project.rdb.batch.model.dto;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.support.SendResult;

public record NotificationSendTask(
        UsageNotificationEvent event, CompletableFuture<SendResult<String, String>> future) {}
