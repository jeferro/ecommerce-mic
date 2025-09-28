package com.jeferro.shared.ddd.application.bus;

import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.application.UseCases;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public abstract class UseCaseBus {

    private static final Logger logger = LoggerFactory.getLogger(UseCaseBus.class);

    protected final UseCases useCases;

    public UseCaseBus() {
        useCases = new UseCases();
    }

    public <R> R execute(Params<R> params) {
        Instant startAt = Instant.now();

        Auth auth = null;
        UseCase<Params<R>, R> useCase = null;

        try {
            auth = createAuth();
            useCase = useCases.findByParams(params);

            ensurePermissions(auth, useCase);

            R result = useCase.execute(auth, params);

            logSuccessExecution(startAt, auth, useCase, params, result);

            return result;
        } catch (Exception cause) {
            logErrorExecution(startAt, auth, useCase, params, cause);

            throw cause;
        }
    }

    protected abstract Auth createAuth();

    private void ensurePermissions(Auth auth, UseCase<?, ?> useCase) {
        var mandatoryRoles = useCase.getMandatoryUserRoles();

        if (!auth.hasRoles(mandatoryRoles)) {
            throw ForbiddenException.createOf(auth, mandatoryRoles);
        }
    }

    private void logSuccessExecution(
            Instant startAt,
            Auth auth,
            UseCase<?, ?> useCase,
            Params<?> params,
            Object result
    ) {
        var useCaseName = useCase.getClass().getSimpleName();
        var duration = calculateDuration(startAt);

        logger.info("""
                \n\t Duration: {}\s
                \t Auth: {}\s
                \t Use case: {}\s
                \t Params: {}\s
                \t Result: {}\s
                """, duration, auth, useCaseName, params, result);
    }

    private void logErrorExecution(
            Instant startAt,
            Auth auth,
            UseCase<?, ?> useCase,
            Params<?> params,
            Exception cause
    ) {
        var useCaseName = useCase != null
                ? useCase.getClass().getSimpleName()
                : "--";

        var duration = calculateDuration(startAt);

        logger.error("""
                \n\t Duration: {}\s
                \t Auth: {}\s
                \t Use case: {}\s
                \t Params: {}
                """, duration, auth, useCaseName, params, cause);
    }

    private Duration calculateDuration(Instant startAt) {
        Instant endAt = Instant.now();

        return Duration.between(startAt, endAt);
    }
}
