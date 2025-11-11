package com.jeferro.shared.ddd.application.execution;

import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.exceptions.ApplicationException;
import com.jeferro.shared.ddd.domain.exceptions.InternalException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.services.ValueValidator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Execution<P extends Params<R>, R> {

  private final Auth auth;

  private final UseCase<P, R> useCase;

  private final P params;

  private final int retries;

  private final List<ExecutionAttempt<R>> attempts;

  private Execution(Auth auth, UseCase<P, R> useCase, P params, int retries, List<ExecutionAttempt<R>> attempts){
	this.auth = auth;
    this.useCase = useCase;
	this.params = params;
	this.retries = retries;
	this.attempts = attempts;
  }

  public static <P extends Params<R>, R> Execution<P, R> create(Auth auth, UseCase<P, R> useCase, P params, int retries) {
    ValueValidator.isNotNull(auth, "auth");
    ValueValidator.isNotNull(useCase, "useCase");
    ValueValidator.isNotNull(params, "params");
    ValueValidator.isPositive(retries, "retries");

    List<ExecutionAttempt<R>> attempts = new ArrayList<>();

    return new Execution<>(auth, useCase, params, retries, attempts);
  }

  public void execute() {
    while (!isEnded()) {
      var attempt = executeAttempt();

      attempts.add(attempt);
    }
  }

  private ExecutionAttempt<R> executeAttempt(){
    var startAt = Instant.now();
    var numAttempt = attempts.size() + 1;

    try {
      var result = useCase.execute(auth, params);

      return ExecutionAttempt.createOfSuccess(numAttempt, startAt, result);
    }
    catch (RuntimeException cause) {
      return ExecutionAttempt.createOfError(numAttempt, startAt, cause);
    }
  }

  public boolean isEnded() {
    if (attempts.isEmpty()) {
      return false;
    }

    if (attempts.size() == retries) {
      return true;
    }

    var lastAttempt = attempts.getLast();

    return lastAttempt.isSuccess();
  }

  public R getResultOrError() {
    var lastAttempt = attempts.getLast();

    if (lastAttempt.isSuccess()) {
      return lastAttempt.getResult();
    }

    var cause = lastAttempt.getCause();

    if (cause instanceof ApplicationException applicationException) {
      throw applicationException;
    }

    throw InternalException.createOf(lastAttempt.getCause());
  }
}
