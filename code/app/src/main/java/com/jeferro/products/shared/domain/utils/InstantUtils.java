package com.jeferro.products.shared.domain.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class InstantUtils {

  public static String toString(Instant date, String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    var zonedDateTime = date.atZone(ZoneId.of("UTC"));

    return zonedDateTime.format(formatter);
  }

  public static Instant parse(String date, String pattern) {
    var zoneId = ZoneId.of("UTC");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    var localDateTime = LocalDateTime.parse(date, formatter);

    return localDateTime.atZone(zoneId).toInstant();
  }
}
