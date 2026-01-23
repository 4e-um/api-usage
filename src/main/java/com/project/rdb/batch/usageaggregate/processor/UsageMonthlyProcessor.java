package com.project.rdb.batch.usageaggregate.processor;

import java.time.format.DateTimeFormatter;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.project.rdb.batch.usageaggregate.dto.UsageLogRow;
import com.project.rdb.batch.usageaggregate.dto.UsageMonthlyAggregation;

@Component
public class UsageMonthlyProcessor implements ItemProcessor<UsageLogRow, UsageMonthlyAggregation> {

    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public UsageMonthlyAggregation process(UsageLogRow log) {
        return new UsageMonthlyAggregation(
                log.subId(), log.eventTime().format(MONTH_FMT), log.usedBytes());
    }
}
