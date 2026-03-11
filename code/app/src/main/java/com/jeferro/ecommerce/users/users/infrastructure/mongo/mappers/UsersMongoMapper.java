package com.jeferro.ecommerce.users.users.infrastructure.mongo.mappers;

import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.domain.models.Username;
import com.jeferro.ecommerce.users.users.infrastructure.mongo.dtos.UserMongoDTO;
import com.jeferro.shared.mappers.AggregateSecondaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public abstract class UsersMongoMapper extends AggregateSecondaryMapper<User, Username, UserMongoDTO> {
}
