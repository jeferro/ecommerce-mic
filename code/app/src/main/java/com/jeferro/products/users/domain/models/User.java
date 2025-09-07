package com.jeferro.products.users.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import lombok.Getter;

import java.util.Set;

@Getter
public class User extends AggregateRoot<Username> {

    private final String encodedPassword;

    private final Set<String> roles;

    public User(Username id, String encodedPassword, Set<String> roles) {
        super(id);

        this.encodedPassword = encodedPassword;
        this.roles = roles;
    }

    public Username getUsername() {
        return id;
    }
}
