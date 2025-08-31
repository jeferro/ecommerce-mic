package com.jeferro.shared.ddd.domain.utils;

import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;

import java.util.Collection;
import java.util.function.Supplier;

public class ValueValidationUtils {

    public static void ensure(Supplier<Boolean> predicate, String message) {
        if(!predicate.get()) {
            throw ValueValidationException.createOfMessage(message);
        }
    }

    public static <T> void isNotBlank(String value, String attributeName) {
        if (value == null || value.isBlank()) {
		  throw ValueValidationException.createOfMessage(attributeName + " is null or blank");
        }
    }

    public static <T> void isNotNull(Object value, String attributeName) {
        if (value == null) {
		  throw ValueValidationException.createOfMessage(attributeName + " is null");
        }
    }

    public static <T> void isNotEmpty(Collection<?> value, String attributeName) {
        if (value == null || value.isEmpty()) {
		  throw ValueValidationException.createOfMessage(attributeName + " is null or empty");
        }
    }

    public static <T> void isZeroOrPositive(Integer value, String attributeName) {
        if (value == null || value < 0) {
		  throw ValueValidationException.createOfMessage(attributeName + " is null or negative");
        }
    }

}
