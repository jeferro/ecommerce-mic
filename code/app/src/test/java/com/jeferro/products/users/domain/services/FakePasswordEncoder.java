package com.jeferro.products.users.domain.services;

public class FakePasswordEncoder implements PasswordEncoder {

  @Override
  public boolean matches(String plainPassword, String encodedPassword) {
    return plainPassword.equals(encodedPassword);
  }
}
