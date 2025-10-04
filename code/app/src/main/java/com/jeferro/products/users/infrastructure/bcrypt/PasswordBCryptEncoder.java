package com.jeferro.products.users.infrastructure.bcrypt;

import com.jeferro.products.users.domain.services.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordBCryptEncoder implements PasswordEncoder {

  private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Override
  public boolean matches(String plainPassword, String encodedPassword) {
    return bCryptPasswordEncoder.matches(plainPassword, encodedPassword);
  }
}
