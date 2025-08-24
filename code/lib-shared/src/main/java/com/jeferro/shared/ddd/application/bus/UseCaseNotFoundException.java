package com.jeferro.shared.ddd.application.bus;

import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class UseCaseNotFoundException extends RuntimeException {

    private UseCaseNotFoundException(String message) {
        super(message);
    }

    public static <T> UseCaseNotFoundException createOfNotFound(Class<T> paramsClazz){
        return new UseCaseNotFoundException("Use case not found for params: " + paramsClazz.getSimpleName());
    }
}
