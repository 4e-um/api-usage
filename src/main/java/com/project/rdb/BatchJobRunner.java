// package com.project.rdb;
//
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.JobParametersBuilder;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
//
// import lombok.RequiredArgsConstructor;
//
// @Component
// @RequiredArgsConstructor
// public class BatchJobRunner implements CommandLineRunner {
//
//    private final JobLauncher jobLauncher;
//    private final Job usageAggregationJob;
//    private final Job usageNotificationJob;
//    private final Job notificationSendJob;
//
//    //////    @Override
//    //////    public void run(String... args) throws Exception {
//    //////        JobParameters params = new JobParametersBuilder()
//    //////                .addString("fromTime", "2025-12-01T00:00:00")
//    //////                .addString("toTime",   "2025-12-31T11:59:59")
//    //////                .addLong("run.id", System.currentTimeMillis())
//    //////                .toJobParameters();
//    //////
//    //////        jobLauncher.run(usageAggregationJob, params);
//    //////    }
//    ////
//    //////    @Override
//    //////    public void run(String... args) throws Exception {
//    //////
//    //////        JobParameters params = new JobParametersBuilder()
//    //////                // 월 요금제 Step용
//    //////                .addString("period", "202512")
//    //////                // 일 요금제 Step용
//    //////                .addString("usageDate", "20251201")
//    //////                .addLong("run.id", System.currentTimeMillis())
//    //////                .toJobParameters();
//    //////
//    //////        jobLauncher.run(usageNotificationJob, params);
//    //////    }
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
// }
