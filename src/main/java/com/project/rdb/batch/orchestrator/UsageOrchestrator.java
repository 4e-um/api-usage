package com.project.rdb.batch.orchestrator;

import java.time.LocalDateTime;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsageOrchestrator {

    private final JobLauncher jobLauncher;
    private final Job usageAggregationJob;
    private final Job usageNotificationJob;
    private final Job notificationSendJob;

    private final BatchTimeWindowService timeWindowService;

    public void run() throws Exception {
        LocalDateTime aggregationStart = LocalDateTime.now();
        LocalDateTime aggFrom = timeWindowService.resolve("usage-aggregation");

        JobParameters aggregationParams =
                new JobParametersBuilder()
                        .addLocalDateTime("fromTime", aggFrom)
                        .addLocalDateTime("toTime", aggregationStart)
                        .addLong("run.id", System.currentTimeMillis())
                        .toJobParameters();

        JobExecution aggregationExec = jobLauncher.run(usageAggregationJob, aggregationParams);

        if (aggregationExec.getStatus().isUnsuccessful()) {
            return;
        }

        // ✅ 성공 시에만 watermark 이동
        timeWindowService.update("usage-aggregation", aggregationStart);

        /* ===============================
         * 2️⃣ 알림 대상 추출 배치
         * =============================== */

        LocalDateTime notificationStart = LocalDateTime.now();

        LocalDateTime notificationFrom = timeWindowService.resolve("usage-notification");

        JobParameters notificationParams =
                new JobParametersBuilder()
                        .addLocalDateTime("fromTime", notificationFrom)
                        .addLocalDateTime("toTime", notificationStart)
                        .addLong("run.id", System.currentTimeMillis())
                        .toJobParameters();

        JobExecution notificationExec = jobLauncher.run(usageNotificationJob, notificationParams);

        if (notificationExec.getStatus().isUnsuccessful()) {
            return;
        }

        timeWindowService.update("usage-notification", notificationStart);

        /* ===============================
         * 3️⃣ 알림 발송 배치
         * =============================== */

        LocalDateTime sendStart = LocalDateTime.now();

        LocalDateTime sendFrom = timeWindowService.resolve("notification-send");

        JobParameters sendParams =
                new JobParametersBuilder()
                        .addLocalDateTime("fromTime", sendFrom)
                        .addLocalDateTime("toTime", sendStart)
                        .addLong("run.id", System.currentTimeMillis())
                        .toJobParameters();

        JobExecution sendExec = jobLauncher.run(notificationSendJob, sendParams);

        if (sendExec.getStatus().isUnsuccessful()) {
            return;
        }

        timeWindowService.update("notification-send", sendStart);
    }
}
