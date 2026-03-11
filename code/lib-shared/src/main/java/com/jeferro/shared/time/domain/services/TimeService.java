package com.jeferro.shared.time.domain.services;

import java.time.Instant;

public abstract class TimeService {

  private static TimeGenerator timeGenerator = new InstantTimeService();

  public static Instant now() {
    return timeGenerator.generate();
  }

  public static void configure(TimeGenerator timeGenerator) {
    TimeService.timeGenerator = timeGenerator;
  }
}
