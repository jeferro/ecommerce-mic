package com.jeferro.shared.ddd.domain.exceptions;

public abstract non-sealed class NotFoundException extends ApplicationException {

  protected NotFoundException(String code, String title, String message) {
    super(code, title, message);
  }
}
