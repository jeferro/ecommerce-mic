package com.jeferro.shared.ddd.application;

import com.jeferro.shared.ddd.application.authorizer.UseCaseAuthorizer;
import com.jeferro.shared.ddd.application.execution.Execution;
import com.jeferro.shared.ddd.application.logger.UseCaseBusLogger;
import com.jeferro.shared.ddd.application.exceptions.UseCaseNotFoundException;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class UseCaseBus {

  public final UseCaseBusLogger logger;

  private final UseCaseAuthorizer authorizer;

  private final Map<Class<Params<?>>, UseCase<?, ?>> useCases;

  public UseCaseBus() {
    logger = new UseCaseBusLogger();
    authorizer = new UseCaseAuthorizer();
    useCases = new HashMap<>();
  }

  public <R> R execute(Params<R> params) {
    return executeWithRetry(params, 1);
  }

  public <R> R executeWithRetry(Params<R> params, int retries) {
	Auth auth = createAuth();

	try {
      UseCase<Params<R>, R> useCase = findUseCaseOrError(params);

      authorizer.ensurePermissions(auth, useCase);

      var execution = Execution.create(auth, useCase, params, retries);
      execution.execute();

      logger.log(execution);

      return execution.getResultOrError();
    }
    catch(UseCaseNotFoundException | ForbiddenException cause) {
      logger.error(auth, params, retries, cause);

	  throw cause;
    }
  }

  protected abstract Auth createAuth();

  protected void registry(UseCase<?, ?> useCase) {
    Type type = useCase.getClass().getGenericSuperclass();

    if (!(type instanceof ParameterizedType parameterizedType)) {
      throw new IllegalArgumentException("Use case superclass is not a parameterized type");
    }

    Class<Params<?>> paramsClass = (Class<Params<?>>) parameterizedType.getActualTypeArguments()[0];

    useCases.put(paramsClass, useCase);
  }

  private <R> UseCase<Params<R>, R> findUseCaseOrError(Params<R> params) {
    UseCase<Params<R>, R> useCase = findUseCase(params);

    if (useCase == null) {
	  throw UseCaseNotFoundException.createOfNotFound(params);
    }

    return useCase;
  }

  public <R> UseCase<Params<R>, R> findUseCase(Params<R> params) {
    Class<?> paramsClass = params.getClass();

    return useCases.containsKey(paramsClass)
        ? (UseCase<Params<R>, R>) useCases.get(paramsClass)
        : null;
  }

}
