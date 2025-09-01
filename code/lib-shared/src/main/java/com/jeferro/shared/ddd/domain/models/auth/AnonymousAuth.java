package com.jeferro.shared.ddd.domain.models.auth;

import java.util.Locale;
import java.util.Set;

public class AnonymousAuth extends Auth {

    public AnonymousAuth(Locale locale) {
        super(locale);
    }

    public static AnonymousAuth create(Locale locale) {
        return new AnonymousAuth(locale);
    }

    @Override
    public String getUsername() {
        return "anonymous";
    }

    @Override
    public boolean hasRoles(Set<String> mandatoryRoles) {
        return mandatoryRoles.isEmpty();
    }
}
