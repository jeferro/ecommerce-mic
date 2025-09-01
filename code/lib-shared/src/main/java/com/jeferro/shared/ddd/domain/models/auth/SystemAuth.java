package com.jeferro.shared.ddd.domain.models.auth;

import java.util.Locale;
import java.util.Set;

public class SystemAuth extends Auth {

    public SystemAuth(Locale locale) {
        super(locale);
    }

    public static SystemAuth create(Locale locale) {
        return new SystemAuth(locale);
    }

    @Override
    public String username() {
        return "system";
    }

    @Override
    public boolean hasRoles(Set<String> mandatoryRoles) {
        return true;
    }
}
