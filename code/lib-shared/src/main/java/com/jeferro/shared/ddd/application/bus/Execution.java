package com.jeferro.shared.ddd.application.bus;

import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Execution<P extends Params<R>, R> {

  private final Auth auth;

  private final P params;

  private final int retries;

  private final List<ExecutionAttempt<R>> attempts;

  public static <P extends Params<R>, R> Execution<P, R> create(Auth auth, P params, int retries) {
    ValueValidator.isPositive(retries, "retries");

    List<ExecutionAttempt<R>> attempts = new ArrayList<>();

    return new Execution<>(auth, params, retries, attempts);
  }

  public void startAttempt() {
    var numAttempt = attempts.size() + 1;

    var attempt = ExecutionAttempt.<R>create(numAttempt);

    attempts.add(attempt);
  }

  public void endAttemptSuccessfully(R result) {
    var lastAttempt = attempts.getLast();

    lastAttempt.markAsSuccess(result);
  }

  public void endAttemptWithError(RuntimeException cause) {
    var lastAttempt = attempts.getLast();

    lastAttempt.markAsError(cause);
  }

  public boolean isEnded() {
    var lastAttempt = attempts.getLast();

    return lastAttempt.isError()
      && attempts.size() >= retries;
  }

  public R getResultOrError() {
    var lastAttempt = attempts.getLast();

    if(lastAttempt.isError()){
      throw lastAttempt.getCause();
    }

    return (R) lastAttempt.getResult();
  }
}
