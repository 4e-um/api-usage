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
public class UsageNotificationMonthlyReaderConfig {

    @Bean(name = "usageNotificationMonthlyReader")
    @StepScope
    public JdbcCursorItemReader<UsageNotificationSource> usageNotificationMonthlyReader(
            DataSource dataSource, @Value("#{jobParameters['period']}") String period) {

        String sql =
                """
                SELECT
                    us.sub_id,
                    us.period,
                    'MONTH' AS unit,
                    us.total_used_bytes,
                    sp.allotment_amount
                FROM usage_summary_monthly us
                JOIN subscription_plan sp
                  ON sp.sub_id = us.sub_id
                WHERE us.period = ?
                  AND sp.allotment_amount > 0
                  AND sp.allotment_amount != 5120
                ORDER BY us.sub_id
                """;

        return new JdbcCursorItemReaderBuilder<UsageNotificationSource>()
                .name("usageNotificationMonthlyReader")
                .dataSource(dataSource)
                .sql(sql)
                .fetchSize(1000)
                .preparedStatementSetter(ps -> ps.setString(1, period))
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
