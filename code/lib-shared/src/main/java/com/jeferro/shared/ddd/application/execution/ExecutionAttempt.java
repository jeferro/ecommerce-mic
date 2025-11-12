package com.jeferro.shared.ddd.application.execution;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class ExecutionAttempt<R> {

  private final int numAttempt;

  private final Instant startAt;

  private Instant endAt;

  private R result;

  private Throwable cause;

  public static <R> ExecutionAttempt<R> createOfSuccess(int numAttempt, Instant startAt, R result) {
	var endAt = Instant.now();

	return new ExecutionAttempt<>(numAttempt, startAt, endAt, result, null);
  }

  public static <R> ExecutionAttempt<R> createOfError(int numAttempt, Instant startAt, Throwable cause) {
	var endAt = Instant.now();

	return new ExecutionAttempt<>(numAttempt, startAt, endAt, null, cause);
  }

  public Duration getDuration() {
	return Duration.between(startAt, endAt);
  }

  public boolean isSuccess() {
	return cause == null;
  }

  public boolean isError() {
	return !isSuccess();
  }
}
