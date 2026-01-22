package com.project.rdb.batch.notificationsend.reader;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.rdb.batch.model.dto.UsageNotificationOutboxRow;

@Configuration
public class NotificationSendReaderConfig {

    @Bean(name = "notificationSendOutboxReader")
    public JdbcCursorItemReader<UsageNotificationOutboxRow> notificationSendOutboxReader(
            DataSource dataSource) {

        String sql =
                """
                    SELECT
                        id,
                        sub_id,
                        period,
                        unit,
                        threshold,
                        percent,
                        total_used_mb,
                        allotment_mb
                    FROM usage_notification_outbox
                    WHERE status = 'PENDING'
                    ORDER BY id
                    """;

        return new JdbcCursorItemReaderBuilder<UsageNotificationOutboxRow>()
                .name("notificationSendOutboxReader")
                .dataSource(dataSource)
                .sql(sql)
                .fetchSize(1_000)
                .rowMapper(
                        (rs, rowNum) ->
                                new UsageNotificationOutboxRow(
                                        rs.getLong("id"),
                                        rs.getLong("sub_id"),
                                        rs.getString("period"),
                                        rs.getString("unit"),
                                        rs.getInt("threshold"),
                                        rs.getInt("percent"),
                                        rs.getLong("total_used_mb"),
                                        rs.getLong("allotment_mb")))
                .build();
    }
}
