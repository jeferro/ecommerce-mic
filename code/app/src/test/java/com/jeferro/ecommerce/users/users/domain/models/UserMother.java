package com.jeferro.ecommerce.users.users.domain.models;

import com.jeferro.ecommerce.shared.domain.models.Roles;

import java.util.Set;

public class UserMother {

  public static User john() {
    var username = UsernameMother.john();
    var roles = Set.of(Roles.USER);

    return new User(username, "encoded-password", roles, 1L, null);
  }

  public static User james() {
    var username = UsernameMother.james();
    var roles = Set.of(Roles.USER);

    return new User(username, "encoded-password", roles, 1L,null);
  }

  public static User emily() {
    var username = UsernameMother.emily();
    var roles = Set.of(Roles.USER, Roles.ADMIN);

    return new User(username, "encoded-password", roles, 1L,null);
  }

  public static User unknown() {
    var username = UsernameMother.unknown();
    var roles = Set.of(Roles.USER);

    return new User(username, "encoded-password", roles, 1L,null);
  }
}
