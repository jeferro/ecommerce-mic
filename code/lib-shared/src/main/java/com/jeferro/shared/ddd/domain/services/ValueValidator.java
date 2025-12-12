package com.jeferro.shared.ddd.domain.services;

import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Supplier;

public class ValueValidator {

  public static void ensure(Supplier<Boolean> predicate, String message) {
    if (!predicate.get()) {
      throw ValueValidationException.createOfMessage(message);
    }
  }

  public static <T> void ensurePositive(int value, String attributeName) {
    if (value < 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is negative: " + value);
    }

    if (value == 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is zero: " + value);
    }
  }

  public static <T> void ensureNotBlank(String value, String attributeName) {
    if (value == null) {
      throw ValueValidationException.createOfMessage(attributeName + " is null");
    }

    if (value.isBlank()) {
      throw ValueValidationException.createOfMessage(attributeName + " is blank");
    }
  }

  public static <T> void ensureNotNull(Object value, String attributeName) {
    if (value == null) {
      throw ValueValidationException.createOfMessage(attributeName + " is null");
    }
  }

  public static <T> void ensureNotEmpty(Collection<?> value, String attributeName) {
    if (value == null) {
      throw ValueValidationException.createOfMessage(attributeName + " is null");
    }

    if (value.isEmpty()) {
      throw ValueValidationException.createOfMessage(attributeName + " is empty");
    }
  }

  public static <T> void ensureZeroOrPositive(Integer value, String attributeName) {
    if (value == null) {
      throw ValueValidationException.createOfMessage(attributeName + " is null");
    }

    if (value < 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is negative");
    }
  }

  public static <T> void ensureZeroOrPositive(BigDecimal value, String attributeName) {
    if (value == null) {
      throw ValueValidationException.createOfMessage(attributeName + " is null");
    }

    if (value.compareTo(BigDecimal.ZERO) < 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is negative");
    }
  }

  public static <T> void ensureInRange(BigDecimal value, int min, int max, String attributeName) {
    if (value == null) {
      throw ValueValidationException.createOfMessage(attributeName + " is null");
    }

    var minDecimal = new BigDecimal(min);

    if (value.compareTo(minDecimal) < 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is less than " + min);
    }

    var maxDecimal = new BigDecimal(max);

    if (value.compareTo(maxDecimal) > 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is greater than " + max);
    }

  }
}
