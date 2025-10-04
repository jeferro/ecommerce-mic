package com.jeferro.shared.ddd.domain.models.auth;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import java.util.Locale;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Auth extends ValueObject {

  private final Locale locale;

  public abstract String getUsername();

  public abstract boolean hasRoles(Set<String> mandatoryRoles);

  @Override
  public String toString() {
    return getUsername();
  }
}
