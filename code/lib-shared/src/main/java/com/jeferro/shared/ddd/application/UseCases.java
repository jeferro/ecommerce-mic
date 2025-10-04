package com.jeferro.shared.ddd.application;

import com.jeferro.shared.ddd.application.params.Params;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UseCases {

  private final Map<Class<Params<?>>, UseCase<?, ?>> useCases;

  public UseCases() {
    useCases = new HashMap<>();
  }

  public void registry(UseCase<?, ?> useCase) {
    Type type = useCase.getClass().getGenericSuperclass();

    if (!(type instanceof ParameterizedType parameterizedType)) {
      throw new IllegalArgumentException("Use case superclass is not a parameterized type");
    }

    Class<Params<?>> paramsClass = (Class<Params<?>>) parameterizedType.getActualTypeArguments()[0];

    useCases.put(paramsClass, useCase);
  }

  public <R> UseCase<Params<R>, R> findByParams(Params<R> params) {
    Class<?> paramsClass = params.getClass();

    return useCases.containsKey(paramsClass)
        ? (UseCase<Params<R>, R>) useCases.get(paramsClass)
        : null;
  }
}
