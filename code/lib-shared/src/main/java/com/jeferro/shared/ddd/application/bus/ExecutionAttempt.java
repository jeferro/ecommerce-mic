package com.jeferro.shared.ddd.application.bus;

import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExecutionAttempt<R> {

  private final int numAttempt;

  private final Instant startAt;

  private Instant endAt;

  private R result;

  private RuntimeException cause;

  public static <R> ExecutionAttempt<R> create(int numAttempt) {
    var now = Instant.now();

    return new ExecutionAttempt<>(numAttempt, now);
  }

  public void markAsSuccess(R result) {
    endAt = Instant.now();

    this.result = result;
  }

  public void markAsError(RuntimeException cause) {
    endAt = Instant.now();

    this.cause = cause;
  }

  public Duration getDuration() {
    return Duration.between(startAt, endAt);
  }

  public boolean isSuccess() {
    return cause == null;
  }

  public boolean isError() {
    return cause != null;
  }
}
