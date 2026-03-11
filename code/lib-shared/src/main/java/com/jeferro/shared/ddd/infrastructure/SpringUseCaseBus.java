package com.jeferro.shared.ddd.infrastructure;

import com.jeferro.shared.auth.infrastructure.ContextManager;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.application.UseCaseBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringUseCaseBus extends UseCaseBus {

  public SpringUseCaseBus(ApplicationContext applicationContext) {
    applicationContext.getBeansOfType(UseCase.class)
        .values()
        .forEach(this::registry);
  }

  @Override
  protected Auth createAuth() {
    return ContextManager.getAuth();
  }
}
