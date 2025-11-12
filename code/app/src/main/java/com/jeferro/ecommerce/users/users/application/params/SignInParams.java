package com.jeferro.ecommerce.users.users.application.params;

import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.domain.models.Username;
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
