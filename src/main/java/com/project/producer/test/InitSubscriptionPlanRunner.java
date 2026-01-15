package com.project.producer.test;

import com.project.producer.PlanChangeProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class InitSubscriptionPlanRunner implements CommandLineRunner {

    private final PlanChangeProducer planChangeProducer;

    private static final int SUB_START = 1;
    private static final int SUB_END = 10_000;

    @Override
    public void run(String... args) {

        OffsetDateTime baseTime = OffsetDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        PlanSeed[] plans = PlanSeed.values();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (long subId = SUB_START; subId <= SUB_END; subId++) {

            PlanSeed plan = plans[random.nextInt(plans.length)];

            planChangeProducer.sendPlanChangeEvent(
                    subId,
                    plan.getUnit(),
                    plan.getAllowance(),
                    baseTime,
                    "user" + subId + "@test.com",
                    "010-" + String.format("%04d-%04d",
                            random.nextInt(10000),
                            random.nextInt(10000))
            );
        }

        System.out.println("✅ Initial Plan Seeding Completed (1 ~ 10000)");
    }
}
