package com.jeferro.shared.ddd.application.logger;

import com.jeferro.shared.ddd.application.UseCaseBus;
import com.jeferro.shared.ddd.application.execution.Execution;
import com.jeferro.shared.ddd.application.execution.ExecutionAttempt;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseCaseBusLogger {

  private static final Logger logger = LoggerFactory.getLogger(UseCaseBus.class);

  public static final String ERROR_FORMAT = "\n\t Params: {} \n\t Attempt: [{} / {}] \n\t Duration: {} \n\t Auth: {}";

  public static final String SUCCESS_FORMAT = "\n\t Params: {} \n\t Attempt: [{} / {}] \n\t Duration: {} \n\t Auth: {} \n\t Result: {}";

  public <R> void error(
	  Auth auth,
	  Params<R> params,
	  int retries,
	  Exception cause) {
	logger.error(ERROR_FORMAT,
		params,
		0,
		retries,
		"--",
		auth,
		cause);
  }

  public <R> void log(Execution<Params<R>, R> execution) {
	execution.getAttempts()
		.forEach(attempt -> logAttempt(execution, attempt));
  }

  private <R> void logAttempt(Execution<Params<R>, R> execution, ExecutionAttempt<R> attempt) {
	if (attempt.isError()) {
	  errorAttempt(execution, attempt);
	  return;
	}

	infoAttempt(execution, attempt);
  }

  private <R> void infoAttempt(Execution<Params<R>, R> execution, ExecutionAttempt<R> attempt) {
    logger.info(SUCCESS_FORMAT,
        execution.getParams(),
        attempt.getNumAttempt(),
        execution.getRetries(),
        attempt.getDuration(),
        execution.getAuth(),
        attempt.getResult());
  }

  private <R> void errorAttempt(Execution<Params<R>, R> execution, ExecutionAttempt<R> attempt) {
    logger.error(ERROR_FORMAT,
        execution.getParams(),
        attempt.getNumAttempt(),
        execution.getRetries(),
        attempt.getDuration(),
        execution.getAuth(),
        attempt.getCause());
  }
}
