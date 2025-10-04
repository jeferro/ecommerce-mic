package com.jeferro.shared.auth.infrastructure.mongo.services;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MongoAuditorAware implements AuditorAware<String> {

  private static final String UNKNOWN = "unknown";

  @Override
  public Optional<String> getCurrentAuditor() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    var value = UNKNOWN;

    if (authentication != null) {
      final Object principal = authentication.getPrincipal();

      if (principal instanceof String) {
        value = (String) principal;
      }
    }

    return Optional.of(value);
  }
}
