package com.jeferro.ecommerce.users.users.infrastructure.rest_api.v1;

import com.jeferro.ecommerce.users.users.infrastructure.rest_api.v1.dtos.*;
import com.jeferro.ecommerce.users.users.infrastructure.rest_api.v1.mappers.AuthRestMapper;
import com.jeferro.shared.auth.infrastructure.rest.jwt.HeaderJwtDecoder;
import com.jeferro.shared.ddd.application.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationsRestController implements AuthenticationApi {

  private final AuthRestMapper authRestMapper;

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
