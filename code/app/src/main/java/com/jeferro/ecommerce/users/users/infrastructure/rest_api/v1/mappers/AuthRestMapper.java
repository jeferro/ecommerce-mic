package com.jeferro.ecommerce.users.users.infrastructure.rest_api.v1.mappers;

import com.jeferro.ecommerce.users.users.application.params.SignInParams;
import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.infrastructure.rest_api.v1.dtos.*;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public abstract class AuthRestMapper {

  public abstract SignInParams toSignInParams(SignInInputRestDTO inputRestDTO);

  @Mapping(target = "token", source = "token")
  public abstract AuthRestDTO toDTO(User user, String token);
}
