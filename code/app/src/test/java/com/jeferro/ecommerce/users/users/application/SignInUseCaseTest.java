package com.jeferro.ecommerce.users.users.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import com.jeferro.ecommerce.users.users.application.params.SignInParams;
import com.jeferro.ecommerce.users.users.domain.models.UserMother;
import com.jeferro.ecommerce.users.users.domain.repositories.UsersFakeRepository;
import com.jeferro.ecommerce.users.users.domain.services.FakePasswordEncoder;
import com.jeferro.shared.ddd.domain.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SignInUseCaseTest {

  private UsersFakeRepository usersFakeRepository;

  private SignInUseCase signInUseCase;

  @BeforeEach
  void beforeEach() {
    usersFakeRepository = new UsersFakeRepository();
    var fakePasswordEncoder = new FakePasswordEncoder();

    signInUseCase = new SignInUseCase(usersFakeRepository, fakePasswordEncoder);
  }

  @Test
  void givenOneUser_whenSignIn_thenReturnsUser() {
    var john = UserMother.john();

    var params = new SignInParams(john.getUsername(), john.getEncodedPassword());

    var result = signInUseCase.execute(AuthMother.anonymous(), params);

    assertEquals(john, result);
  }

  @Test
  void givenAnAuthenticatedUser_whenSignIn_thenReturnsUser() {
    var john = UserMother.john();

    var params = new SignInParams(john.getUsername(), john.getEncodedPassword());

    var result = signInUseCase.execute(AuthMother.john(), params);

    assertEquals(john, result);
  }

  @Test
  void givenUnknownUsers_whenSignIn_thenThrowsUnauthorizedException() {
    var unknown = UserMother.unknown();

    var params = new SignInParams(unknown.getUsername(), unknown.getEncodedPassword());

    assertThrows(
        UnauthorizedException.class, () -> signInUseCase.execute(AuthMother.anonymous(), params));
  }

  @Test
  void givenWrongCredentials_whenSignIn_thenThrowsUnauthorizedException() {
    var john = UserMother.john();

    var params = new SignInParams(john.getUsername(), "wrong-password");

    assertThrows(
        UnauthorizedException.class, () -> signInUseCase.execute(AuthMother.anonymous(), params));
  }
}
