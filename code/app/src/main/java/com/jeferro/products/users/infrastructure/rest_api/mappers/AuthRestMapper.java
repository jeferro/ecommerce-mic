package com.jeferro.products.users.infrastructure.rest_api.mappers;

import com.jeferro.products.users.application.params.SignInParams;
import com.jeferro.products.users.domain.models.User;
import com.jeferro.products.users.infrastructure.rest_api.dtos.AuthRestDTO;
import com.jeferro.products.users.infrastructure.rest_api.dtos.SignInInputRestDTO;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class AuthRestMapper {

  public static final AuthRestMapper INSTANCE = Mappers.getMapper(AuthRestMapper.class);

  public abstract SignInParams toSignInParams(SignInInputRestDTO inputRestDTO);

  @Mapping(target = "token", source = "token")
  public abstract AuthRestDTO toDTO(User user, String token);
}
