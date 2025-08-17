package com.jeferro.products.shared.domain.models.auth;

import com.jeferro.shared.ddd.domain.models.auth.AnonymousAuth;
import com.jeferro.shared.ddd.domain.models.auth.SystemAuth;
import com.jeferro.shared.ddd.domain.models.auth.UserAuth;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.ADMIN;
import static com.jeferro.products.shared.application.Roles.USER;

public class AuthMother {

    public static UserAuth john() {
        var roles = Set.of(USER);

        return new UserAuth("john", roles);
    }

    public static UserAuth james() {
        var roles = Set.of(USER);

        return new UserAuth("james", roles);
    }

    public static UserAuth emily() {
        var roles = Set.of(ADMIN);

        return new UserAuth("emily", roles);
    }

    public static AnonymousAuth anonymous() {
        return new AnonymousAuth();
    }

    public static SystemAuth system() {
        return new SystemAuth();
    }

}
