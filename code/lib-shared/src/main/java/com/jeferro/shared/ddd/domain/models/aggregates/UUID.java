package com.jeferro.shared.ddd.domain.models.aggregates;

public abstract class UUID extends StringIdentifier {

  public UUID(String value) {
    super(value);
  }

  protected static String generateUuid() {
    return java.util.UUID.randomUUID().toString();
  }
}
