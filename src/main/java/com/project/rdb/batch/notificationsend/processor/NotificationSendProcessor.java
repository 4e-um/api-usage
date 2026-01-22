package com.project.rdb.batch.notificationsend.processor;

import java.util.UUID;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.project.rdb.batch.model.dto.UsageNotificationEvent;
import com.project.rdb.batch.model.dto.UsageNotificationOutboxRow;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationSendProcessor
        implements ItemProcessor<UsageNotificationOutboxRow, UsageNotificationEvent> {

    @Override
    public UsageNotificationEvent process(UsageNotificationOutboxRow item) {

        return new UsageNotificationEvent(
                UUID.randomUUID(),
                item.id(),
                item.subId(),
                item.period(),
                item.unit(),
                item.threshold(),
                item.percent(),
                item.totalUsedMb(),
                item.allotmentMb());
    }
}
