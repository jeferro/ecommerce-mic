package com.jeferro.ecommerce.users.users.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import java.util.Set;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringExclude;

@Getter
public class User extends AggregateRoot<Username> {

  @ToStringExclude
  private final String encodedPassword;

  private final Set<String> roles;

  public User(Username id, String encodedPassword, Set<String> roles, long version, Metadata metadata) {
    super(id, version, metadata);

    this.encodedPassword = encodedPassword;
    this.roles = roles;
  }

  public Username getUsername() {
    return id;
  }
}
