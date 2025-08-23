package com.jeferro.products.users.users.application;

import com.jeferro.products.shared.application.ContextMother;
import com.jeferro.products.users.users.application.params.SignInParams;
import com.jeferro.products.users.users.domain.models.UserMother;
import com.jeferro.products.users.users.domain.repositories.UsersInMemoryRepository;
import com.jeferro.products.users.users.domain.services.FakePasswordEncoder;
import com.jeferro.shared.ddd.domain.exceptions.auth.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SignInUseCaseTest {

    private UsersInMemoryRepository usersInMemoryRepository;

    private SignInUseCase signInUseCase;

    @BeforeEach
    void beforeEach() {
        usersInMemoryRepository = new UsersInMemoryRepository();
        var fakePasswordEncoder = new FakePasswordEncoder();

        signInUseCase = new SignInUseCase(usersInMemoryRepository, fakePasswordEncoder);
    }

    @Test
    void givenOneUser_whenSignIn_thenReturnsUser() {
        var john = UserMother.john();

        var params = new SignInParams(
                john.getUsername(),
                john.getEncodedPassword()
        );

        var result = signInUseCase.execute(
            ContextMother.anonymous(),
            params);

        assertEquals(john, result);
    }

    @Test
    void givenAnAuthenticatedUser_whenSignIn_thenReturnsUser() {
        var john = UserMother.john();

        var params = new SignInParams(
                john.getUsername(),
                john.getEncodedPassword()
        );

        var result = signInUseCase.execute(
            ContextMother.john(),
            params);

        assertEquals(john, result);
    }

    @Test
    void givenUnknownUsers_whenSignIn_thenThrowsUnauthorizedException() {
        var unknown = UserMother.unknown();

        var params = new SignInParams(
                unknown.getUsername(),
                unknown.getEncodedPassword()
        );

        assertThrows(UnauthorizedException.class,
                () -> signInUseCase.execute(
                    ContextMother.anonymous(),
                    params));
    }

    @Test
    void givenWrongCredentials_whenSignIn_thenThrowsUnauthorizedException() {
        var john = UserMother.john();

        var params = new SignInParams(
                john.getUsername(),
                "wrong-password"
        );

        assertThrows(UnauthorizedException.class,
                () -> signInUseCase.execute(
                    ContextMother.anonymous(),
                    params));
    }
}
