package com.jeferro.shared.mongo.audition;

import java.util.Optional;

import com.jeferro.shared.auth.infrastructure.ContextManager;
import org.springframework.data.domain.AuditorAware;

public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(ContextManager.getAuth().getUsername());
  }
}
