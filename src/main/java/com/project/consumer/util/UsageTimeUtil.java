package com.project.consumer.util;

import java.time.Duration;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class UsageTimeUtil {
  private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

  public static String toYyyyMM(String isoTs) {
    OffsetDateTime odt = OffsetDateTime.parse(isoTs, ISO);
    return odt.format(DateTimeFormatter.ofPattern("yyyyMM"));
  }

  public static long ttlToNextMonthWithBufferSec(String isoTs, int bufferDays) {
    OffsetDateTime now = OffsetDateTime.parse(isoTs, ISO);

    OffsetDateTime nextMonthStart = now.withDayOfMonth(1).with(LocalTime.MIDNIGHT).plusMonths(1);

    Duration base = Duration.between(now, nextMonthStart);
    Duration buffer = Duration.ofDays(bufferDays);

    return Math.max(base.plus(buffer).getSeconds(), 3600);
  }
}
