package com.jeferro.products.users.infrastructure.mongo.mappers;

import com.jeferro.products.users.domain.models.User;
import com.jeferro.products.users.domain.models.Username;
import com.jeferro.products.users.infrastructure.mongo.dtos.UserMongoDTO;
import com.jeferro.shared.mappers.AggregateSecondaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class UsersMongoMapper extends AggregateSecondaryMapper<User, Username, UserMongoDTO> {

  public static final UsersMongoMapper INSTANCE = Mappers.getMapper(UsersMongoMapper.class);
}
