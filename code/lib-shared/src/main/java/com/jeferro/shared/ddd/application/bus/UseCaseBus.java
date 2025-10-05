package com.jeferro.shared.ddd.application.bus;

import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.application.UseCases;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UseCaseBus {

  private static final Logger logger = LoggerFactory.getLogger(UseCaseBus.class);

  protected final UseCases useCases;

  public UseCaseBus() {
    useCases = new UseCases();
  }

  public <R> R execute(Params<R> params) {
    return executeWithRetry(params, 1);
  }

  public <R> R executeWithRetry(Params<R> params, int retries) {
    Auth auth = createAuth();

    UseCase<Params<R>, R> useCase = fidUseCaseOrError(params, auth, retries);

    ensurePermissions(params, auth, retries, useCase);

    var execution = Execution.create(auth, params, retries);

    while (!execution.isEnded()) {
      try {
        execution.startAttempt();

        R result = useCase.execute(auth, params);

        execution.endAttemptSuccessfully(result);
      } catch (RuntimeException cause) {
        execution.endAttemptWithError(cause);
      }
    }

    logExecution(execution);

    return execution.getResultOrError();
  }

  protected abstract Auth createAuth();

  private <R> UseCase<Params<R>, R> fidUseCaseOrError(Params<R> params, Auth auth, int retries) {
    UseCase<Params<R>, R> useCase = useCases.findByParams(params);

    if (useCase == null) {
      var useCaseNotFoundError = UseCaseNotFoundException.createOfNotFound(params);

      logPreviousError(auth, params, retries, useCaseNotFoundError);

      throw useCaseNotFoundError;
    }

    return useCase;
  }

  private <R> void ensurePermissions(
      Params<R> params, Auth auth, int retries, UseCase<Params<R>, R> useCase) {
    var mandatoryRoles = useCase.getMandatoryUserRoles();

    if (!auth.hasRoles(mandatoryRoles)) {
      var forbiddenError = ForbiddenException.createOf(auth, mandatoryRoles);

      logPreviousError(auth, params, retries, forbiddenError);

      throw forbiddenError;
    }
  }

  private <R> void logExecution(Execution<Params<R>, R> execution) {
    execution.getAttempts().forEach(attempt -> logAttempt(execution, attempt));
  }

  private <R> void logAttempt(Execution<Params<R>, R> execution, ExecutionAttempt<R> attempt) {
    if (attempt.isError()) {
      logError(
          attempt.getDuration(),
          execution.getAuth(),
          execution.getParams(),
          attempt.getNumAttempt(),
          execution.getRetries(),
          attempt.getCause());
    } else {
      logSuccessAttempt(execution, attempt);
    }
  }

  private <R> void logSuccessAttempt(
      Execution<Params<R>, R> execution, ExecutionAttempt<R> attempt) {
    logger.info(
        "\n\t Params: {} \n\t Attempt: [{} / {}] \n\t Duration: {} \n\t Auth: {} \n\t Result: {}",
        execution.getParams(),
        attempt.getNumAttempt(),
        execution.getRetries(),
        attempt.getDuration(),
        execution.getAuth(),
        attempt.getResult());
  }

  private <R> void logPreviousError(Auth auth, Params<R> params, int retries, Exception cause) {
    logError(null, auth, params, 0, retries, cause);
  }

  private <R> void logError(
      Duration duration,
      Auth auth,
      Params<R> params,
      int numAttempt,
      int retries,
      Exception cause) {
    logger.error(
        "\n\t Params: {} \n\t Attempt: [{} / {}] \n\t Duration: {} \n\t Auth: {}",
        params,
        numAttempt,
        retries,
        duration,
        auth,
        cause);
  }
}
