package com.jeferro.ecommerce.users.users.application;

import com.jeferro.ecommerce.users.users.application.params.SignInParams;
import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.domain.repositories.UsersRepository;
import com.jeferro.ecommerce.users.users.domain.services.PasswordEncoder;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.exceptions.UnauthorizedException;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInUseCase extends UseCase<SignInParams, User> {

  private final UsersRepository usersRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of();
  }

  @Override
  public User execute(Auth auth, SignInParams params) {
    var user = findUserOrError(params);

    ensurePasswordIsCorrect(params, user);

    return user;
  }

  private User findUserOrError(SignInParams params) {
    var username = params.getUsername();

    return usersRepository.findById(username)
        .orElseThrow(UnauthorizedException::createOfUserNotFound);
  }

  private void ensurePasswordIsCorrect(SignInParams params, User user) {
    var plainPassword = params.getPassword();

    var matches = passwordEncoder.matches(plainPassword, user.getEncodedPassword());

    if (!matches) {
      throw UnauthorizedException.createOfWrongPassword();
    }
  }
}
