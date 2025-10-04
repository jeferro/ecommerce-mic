package com.jeferro.products.shared.application;

import com.jeferro.shared.ddd.application.bus.UseCaseBus;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import jakarta.el.MethodNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class StubUseCaseBus extends UseCaseBus {

  private final List<Params<?>> params;

  private Object result;

  public StubUseCaseBus() {
    params = new ArrayList<>();
    result = null;
  }

  @Override
  public <R> R execute(Params<R> params) {
    this.params.add(params);

    return (R) result;
  }

  @Override
  protected Auth createAuth() {
    throw new MethodNotFoundException();
  }

  public void init(Object result) {
    params.clear();

    this.result = result;
  }

  public int size() {
    return params.size();
  }

  public Params<?> getFirstParamsOrError() {
    return params.stream().findFirst().orElseThrow();
  }
}
