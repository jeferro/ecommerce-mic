package com.jeferro.products.shared.application;

import com.jeferro.products.shared.domain.models.auth.AuthMother;
import com.jeferro.shared.ddd.domain.models.context.Context;

import java.util.Locale;

public class ContextMother {

    public static Context john() {
        var auth = AuthMother.john();
        var locale = Locale.of("en", "US");

        return new Context(auth, locale);
    }

    public static Context james() {
        var auth = AuthMother.james();
        var locale = Locale.of("en", "US");

        return new Context(auth, locale);
    }

    public static Context emily() {
        var auth = AuthMother.emily();
        var locale = Locale.of("en", "US");

        return new Context(auth, locale);
    }

    public static Context anonymous() {
        var auth = AuthMother.anonymous();
        var locale = Locale.of("en", "US");

        return new Context(auth, locale);
    }
}
