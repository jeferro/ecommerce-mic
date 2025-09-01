package com.jeferro.products.shared.domain.models.auth;

import com.jeferro.shared.ddd.domain.models.auth.AnonymousAuth;
import com.jeferro.shared.ddd.domain.models.auth.SystemAuth;
import com.jeferro.shared.ddd.domain.models.auth.UserAuth;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.ADMIN;
import static com.jeferro.products.shared.application.Roles.USER;
import static java.util.Locale.ENGLISH;

public class AuthMother {

    public static UserAuth john() {
        var roles = Set.of(USER);

        return new UserAuth(ENGLISH, "john", roles);
    }

    public static UserAuth james() {
        var roles = Set.of(USER);

        return new UserAuth(ENGLISH, "james", roles);
    }

    public static UserAuth emily() {
        var roles = Set.of(ADMIN);

        return new UserAuth(ENGLISH, "emily", roles);
    }

    public static AnonymousAuth anonymous() {
        return new AnonymousAuth(ENGLISH);
    }

    public static SystemAuth system() {
        return new SystemAuth(ENGLISH);
    }

}
