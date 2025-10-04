package com.jeferro.products.shared.domain.services.time;

import com.jeferro.shared.time.domain.services.TimeGenerator;
import com.jeferro.shared.time.domain.services.TimeService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class FakeTimeService implements TimeGenerator {

  public static Instant fakesNow() {
    var now = Instant.now();

    var instance = new FakeTimeService(now);

    TimeService.configure(instance);

    return TimeService.now();
  }

  public static Instant fakesOneMinuteLater() {
    var oneMinuteLater = Instant.now().plus(1, ChronoUnit.MINUTES);

    var instance = new FakeTimeService(oneMinuteLater);

    TimeService.configure(instance);

    return TimeService.now();
  }

  private final Instant now;

  private FakeTimeService(Instant now) {
    this.now = now;
  }

  @Override
  public Instant generate() {
    return now;
  }
}
