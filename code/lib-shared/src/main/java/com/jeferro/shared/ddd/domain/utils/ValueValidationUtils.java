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

    public static void isNotBlank(String value, String attributeName, Object obj) {
        isNotBlank(value, attributeName, obj.getClass());
    }

    public static <T> void isNotBlank(String value, String attributeName, Class<T> clazz) {
        if (value == null || value.isBlank()) {
            String message = formatAttributeName(attributeName, clazz) + " is null or blank";
            throw ValueValidationException.createOfMessage(message);
        }
    }

    public static void isNotNull(Object value, String attributeName, Object obj) {
        isNotNull(value, attributeName, obj.getClass());
    }

    public static <T> void isNotNull(Object value, String attributeName, Class<T> clazz) {
        if (value == null) {
            String message = formatAttributeName(attributeName, clazz) + " is null";
            throw ValueValidationException.createOfMessage(message);
        }
    }

    public static void isNotEmpty(Collection<?> value, String attributeName, Object obj) {
        isNotEmpty(value, attributeName, obj.getClass());
    }

    public static <T> void isNotEmpty(Collection<?> value, String attributeName, Class<T> clazz) {
        if (value == null || value.isEmpty()) {
            String message = formatAttributeName(attributeName, clazz) + " is null or empty";
            throw ValueValidationException.createOfMessage(message);
        }
    }

    public static void isZeroOrPositive(Integer value, String attributeName, Object obj) {
        isZeroOrPositive(value, attributeName, obj.getClass());
    }

    public static <T> void isZeroOrPositive(Integer value, String attributeName, Class<T> clazz) {
        if (value == null || value < 0) {
            String message = formatAttributeName(attributeName, clazz) + " is null or negative";
            throw ValueValidationException.createOfMessage(message);
        }
    }

    private static <T> String formatAttributeName(String attributeName, Class<T> clazz) {
        return clazz.getSimpleName() + "." + attributeName;
    }
}
