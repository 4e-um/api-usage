package com.project.rdb;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.rdb.batch.orchestrator.UsageOrchestrator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatchJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job usageAggregationJob;
    private final Job usageNotificationJob;
    private final Job notificationSendJob;
    private final UsageOrchestrator usageOrchestrator;

    @Override
    public void run(String... args) throws Exception {
        usageOrchestrator.run();
    }

    //        @Override
    //        public void run(String... args) throws Exception {
    //
    //            JobParameters params = new JobParametersBuilder()
    //                    // 월 요금제 Step용
    //                    .addString("fromTime", "2026-01-22T00:00:00")
    //                    .addString("toTime",   "2026-01-22T23:59:59")
    //                    .addLong("run.id", System.currentTimeMillis())
    //                    .toJobParameters();
    //
    //            jobLauncher.run(usageNotificationJob, params);
    //        }
    //    //
    //    @Override
    //    public void run(String... args) throws Exception {
    //        JobParameters params =
    //                new JobParametersBuilder()
    //                        .addLong("runAt", System.currentTimeMillis()) // 중복 실행 방지
    //                        .toJobParameters();
    //
    //        jobLauncher.run(notificationSendJob, params);
    //    }
}
