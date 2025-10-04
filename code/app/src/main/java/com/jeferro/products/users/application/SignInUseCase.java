package com.jeferro.products.users.application;

import com.jeferro.products.users.application.params.SignInParams;
import com.jeferro.products.users.domain.models.User;
import com.jeferro.products.users.domain.models.Username;
import com.jeferro.products.users.domain.repositories.UsersRepository;
import com.jeferro.products.users.domain.services.PasswordEncoder;
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
    var username = params.getUsername();
    var plainPassword = params.getPassword();

    var user = ensureUserExists(username);

    ensurePasswordIsCorrect(plainPassword, user);

    return user;
  }

  private User ensureUserExists(Username username) {
    return usersRepository.findById(username).orElseThrow(UnauthorizedException::createOf);
  }

  private void ensurePasswordIsCorrect(String plainPassword, User user) {
    var matches = passwordEncoder.matches(plainPassword, user.getEncodedPassword());

    if (!matches) {
      throw UnauthorizedException.createOf();
    }
  }
}
