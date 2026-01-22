package com.project.rdb.batch.usagenotification.reader;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.rdb.batch.usagenotification.dto.UsageNotificationSource;

@Configuration
public class UsageNotificationMonthlyReaderConfig {

    @Bean(name = "usageNotificationMonthlyReader")
    @StepScope
    public JdbcCursorItemReader<UsageNotificationSource> usageNotificationMonthlyReader(
            DataSource dataSource,
            @Value("#{jobParameters['fromTime']}") String fromTime,
            @Value("#{jobParameters['toTime']}")   String toTime) {

        String sql =
                """
                SELECT
                    usm.sub_id,
                    usm.period,
                    'MONTH' AS unit,
                    sp.plan_name,
                    usm.total_used_bytes,
                    sp.allotment_amount
                FROM usage_summary_monthly usm
                JOIN subscription_plan sp
                  ON sp.sub_id = usm.sub_id
                WHERE usm.updated_at >= ?::timestamp
                  AND usm.updated_at <  ?::timestamp
                  AND sp.allotment_amount > 0
                  AND sp.allotment_amount != 5120
                ORDER BY usm.sub_id
                """;

        return new JdbcCursorItemReaderBuilder<UsageNotificationSource>()
                .name("usageNotificationMonthlyReader")
                .dataSource(dataSource)
                .sql(sql)
                .fetchSize(1000)
                .preparedStatementSetter(ps -> {
                    ps.setObject(1, fromTime);
                    ps.setObject(2, toTime);
                })
                .rowMapper(
                        (rs, rowNum) ->
                                new UsageNotificationSource(
                                        rs.getLong("sub_id"),
                                        rs.getString("period"),
                                        rs.getString("unit"),
                                        rs.getString("plan_name"),
                                        rs.getLong("total_used_bytes"),
                                        rs.getLong("allotment_amount")))
                .build();
    }
}
