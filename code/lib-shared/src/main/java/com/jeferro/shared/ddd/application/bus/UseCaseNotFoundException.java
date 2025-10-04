package com.jeferro.shared.ddd.application.bus;

import lombok.Getter;

@Getter
public class UseCaseNotFoundException extends RuntimeException {

  private UseCaseNotFoundException(String message) {
    super(message);
  }

  public static <T> UseCaseNotFoundException createOfNotFound(T params) {
    var paramsClass = params.getClass();

    return new UseCaseNotFoundException(
        "Use case not found for params: " + paramsClass.getSimpleName());
  }
}
