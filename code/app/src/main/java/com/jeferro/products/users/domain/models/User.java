package com.jeferro.products.users.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import java.util.Set;

import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import lombok.Getter;

@Getter
public class User extends AggregateRoot<Username> {

  private final String encodedPassword;

  private final Set<String> roles;

  public User(Username id, String encodedPassword, Set<String> roles, Metadata metadata) {
    super(id, metadata);

    this.encodedPassword = encodedPassword;
    this.roles = roles;
  }

  public Username getUsername() {
    return id;
  }
}
