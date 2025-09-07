package com.jeferro.products.users.domain.services;

public interface PasswordEncoder {

    boolean matches(String plainPassword, String encodedPassword);
}
