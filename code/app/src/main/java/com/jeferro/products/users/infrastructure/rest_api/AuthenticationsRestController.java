package com.jeferro.products.users.infrastructure.rest_api;

import com.jeferro.products.users.infrastructure.rest_api.dtos.AuthRestDTO;
import com.jeferro.products.users.infrastructure.rest_api.dtos.SignInInputRestDTO;
import com.jeferro.products.users.infrastructure.rest_api.mappers.AuthRestMapper;
import com.jeferro.shared.auth.infrastructure.rest.jwt.HeaderJwtDecoder;
import com.jeferro.shared.ddd.application.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationsRestController implements AuthenticationApi {

  private final AuthRestMapper authRestMapper = AuthRestMapper.INSTANCE;

  private final HeaderJwtDecoder headerJwtDecoder;

  private final UseCaseBus useCaseBus;

  @Override
  public AuthRestDTO authenticate(
      @RequestBody SignInInputRestDTO signInInputRestDTO) {
    var params = authRestMapper.toSignInParams(signInInputRestDTO);

    var user = useCaseBus.execute(params);

    var token = headerJwtDecoder.encode(user.getUsername().toString(), user.getRoles());

    return authRestMapper.toDTO(user, token);
  }
}
