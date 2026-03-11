package com.jeferro.ecommerce.users.users.domain.services;

public interface PasswordEncoder {

  boolean matches(String plainPassword, String encodedPassword);
}
