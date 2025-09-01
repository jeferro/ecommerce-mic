package com.jeferro.shared.ddd.application;

import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class UseCase<P extends Params<R>, R> {

    protected final Logger logger;

    public UseCase() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public abstract Set<String> getMandatoryUserRoles();

    public abstract R execute(Auth auth, P params);

}
