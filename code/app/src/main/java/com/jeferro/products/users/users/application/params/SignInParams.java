package com.jeferro.products.users.users.application.params;

import com.jeferro.products.users.users.domain.models.User;
import com.jeferro.products.users.users.domain.models.Username;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class SignInParams extends Params<User> {

    private final Username username;

    private final String password;

    public SignInParams(Username username, String password) {
        super();

        ValueValidationUtils.isNotNull(username, "username");
        ValueValidationUtils.isNotNull(username, "username");

        this.username = username;
        this.password = password;
    }
}
