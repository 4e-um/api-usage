package com.project.rdb.batch.usageaggregate.processor;

import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.project.rdb.batch.model.dto.UsageDailyAggregation;
import com.project.rdb.batch.model.dto.UsageLogRow;

@Component
public class UsageDailyProcessor implements ItemProcessor<UsageLogRow, UsageDailyAggregation> {

    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public UsageDailyAggregation process(UsageLogRow log) {
        return new UsageDailyAggregation(
                log.subId(), log.eventTime().format(DAY_FMT), log.usedBytes());
    }
}
