package com.jeferro.shared.ddd.domain.models.auth;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.Set;

@Getter
@AllArgsConstructor
public abstract class Auth extends ValueObject {

    private final Locale locale;

    public abstract String username();

    public abstract boolean hasRoles(Set<String> mandatoryRoles);

    @Override
    public String toString() {
        return username();
    }
}
