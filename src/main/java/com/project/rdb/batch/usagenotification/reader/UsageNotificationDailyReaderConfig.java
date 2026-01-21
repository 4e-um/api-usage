package com.project.rdb.batch.usagenotification.reader;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.rdb.batch.model.dto.UsageNotificationSource;

@Configuration
public class UsageNotificationDailyReaderConfig {

    @Bean(name = "usageNotificationDailyReader")
    @StepScope
    public JdbcCursorItemReader<UsageNotificationSource> usageNotificationDailyReader(
            DataSource dataSource, @Value("#{jobParameters['usageDate']}") String usageDate) {

        String sql =
                """
                SELECT
                    usd.sub_id,
                    usd.usage_date AS period,
                    'DAY' AS unit,
                    usd.total_used_bytes,
                    sp.allotment_amount
                FROM usage_summary_daily usd
                JOIN subscription_plan sp
                  ON sp.sub_id = usd.sub_id
                WHERE usd.usage_date = ?
                  AND sp.allotment_amount = 5120
                ORDER BY usd.sub_id
                """;

        return new JdbcCursorItemReaderBuilder<UsageNotificationSource>()
                .name("usageNotificationDailyReader")
                .dataSource(dataSource)
                .sql(sql)
                .fetchSize(1000)
                .preparedStatementSetter(ps -> ps.setString(1, usageDate))
                .rowMapper(
                        (rs, rowNum) ->
                                new UsageNotificationSource(
                                        rs.getLong("sub_id"),
                                        rs.getString("period"),
                                        rs.getString("unit"),
                                        rs.getLong("total_used_bytes"),
                                        rs.getLong("allotment_amount")))
                .build();
    }
}
