package com.jeferro.products.users.application.params;

import com.jeferro.products.users.domain.models.User;
import com.jeferro.products.users.domain.models.Username;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class SignInParams extends Params<User> {

    private final Username username;

    private final String password;

    public SignInParams(Username username, String password) {
        super();

        ValueValidator.isNotNull(username, "username");
        ValueValidator.isNotNull(username, "username");

        this.username = username;
        this.password = password;
    }
}
