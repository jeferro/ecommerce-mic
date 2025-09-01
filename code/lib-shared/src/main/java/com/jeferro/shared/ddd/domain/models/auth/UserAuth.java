package com.jeferro.shared.ddd.domain.models.auth;

import lombok.Getter;

import java.util.Locale;
import java.util.Set;

@Getter
public class UserAuth extends Auth {

    private final String username;

    private final Set<String> roles;

    public UserAuth(Locale locale, String username, Set<String> roles) {
        super(locale);

        this.username = username;
        this.roles = roles;
    }

    public static UserAuth create(Locale locale, String username, Set<String> roles) {
        return new UserAuth(locale, username, roles);
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public boolean hasRoles(Set<String> mandatoryRoles) {
        return mandatoryRoles.isEmpty()
                || roles.containsAll(mandatoryRoles);
    }
}
