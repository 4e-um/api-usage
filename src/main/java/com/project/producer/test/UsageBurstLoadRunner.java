package com.project.producer.test;

import com.project.producer.UsageProducer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsageBurstLoadRunner implements CommandLineRunner {

  private final UsageProducer usageProducer;

  private static final int EVENTS_PER_SECOND = 10_000;
  private static final int DURATION_SECONDS = 60;
  private static final int THREADS = 8;

  @Override
  public void run(String... args) throws Exception {

    ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    AtomicInteger sent = new AtomicInteger();

    int perThreadRate = EVENTS_PER_SECOND / THREADS;

    long start = System.currentTimeMillis();

    for (int t = 0; t < THREADS; t++) {
      executor.submit(
          () -> {
            ThreadLocalRandom random = ThreadLocalRandom.current();

            long endTime = System.currentTimeMillis() + DURATION_SECONDS * 1000L;

            while (System.currentTimeMillis() < endTime) {
              long secondStart = System.nanoTime();

              for (int i = 0; i < perThreadRate; i++) {
                long subId = random.nextLong(1, 1_000_001);
                long bytes = random.nextLong(200, 5_000);

                usageProducer.sendUsageEvent(subId, bytes, null, 0);

                sent.incrementAndGet();
              }

              long elapsedNs = System.nanoTime() - secondStart;
              long sleepMs = 1000 - (elapsedNs / 1_000_000);

              if (sleepMs > 0) {
                try {
                  Thread.sleep(sleepMs);
                } catch (InterruptedException ignored) {
                }
              }
            }
          });
    }

    executor.shutdown();
    executor.awaitTermination(DURATION_SECONDS + 10, TimeUnit.SECONDS);

    long elapsed = System.currentTimeMillis() - start;

    System.out.println("✅ DONE");
    System.out.println("Total events sent = " + sent.get());
    System.out.println("Elapsed ms = " + elapsed);
    System.out.println("Avg TPS = " + (sent.get() * 1000L / elapsed));
  }
}
