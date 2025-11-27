package com.jeferro.shared.ddd.application.authorizer;

import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.exceptions.ForbiddenException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;

public class UseCaseAuthorizer {

  public <R> void ensurePermissions(Auth auth, UseCase<Params<R>, R> useCase) {
    var mandatoryRoles = useCase.getMandatoryUserRoles();

    if (!auth.hasRoles(mandatoryRoles)) {
	  throw ForbiddenException.createOf(auth, mandatoryRoles);
    }
  }
}
