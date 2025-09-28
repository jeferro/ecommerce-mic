package com.jeferro.shared.ddd.infrastructure.rest;

import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.exceptions.InternalError;
import com.jeferro.shared.ddd.domain.exceptions.NotFoundException;
import com.jeferro.shared.ddd.domain.exceptions.UnauthorizedException;
import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;
import com.jeferro.shared.ddd.infrastructure.rest.mappers.ProblemDetailRestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ErrorRestController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorRestController.class);

    public static ProblemDetailRestMapper problemDetailRestMapper = ProblemDetailRestMapper.INSTANCE;

    @ResponseBody
    @ExceptionHandler(value = {
            ValueValidationException.class,
            ServerWebInputException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<ProblemDetail> handleBadRequest(Exception cause) {
        return problemDetailRestMapper.toDTO(HttpStatus.BAD_REQUEST, cause);
    }

    @ResponseBody
    @ExceptionHandler(value = {
            UnauthorizedException.class
    })
    public ResponseEntity<ProblemDetail> handleUnauthorized(Exception cause) {
        return problemDetailRestMapper.toDTO(HttpStatus.UNAUTHORIZED, cause);
    }

    @ResponseBody
    @ExceptionHandler(value = {
            ForbiddenException.class
    })
    public ResponseEntity<ProblemDetail> handleForbidden(Exception cause) {
        return problemDetailRestMapper.toDTO(HttpStatus.FORBIDDEN, cause);
    }

    @ResponseBody
    @ExceptionHandler(value = {
            NotFoundException.class,
            NoResourceFoundException.class
    })
    public ResponseEntity<ProblemDetail> handleNotFound(Exception cause) {
        return problemDetailRestMapper.toDTO(HttpStatus.NOT_FOUND, cause);
    }

    @ResponseBody
    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseEntity<ProblemDetail> handleException(Exception cause) {
        if(!(cause instanceof InternalError)) {
            logger.error("Catch an unknown exception", cause);
        }

        return problemDetailRestMapper.toDTO(HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

}
