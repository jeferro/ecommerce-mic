package com.jeferro.ecommerce.users.users.infrastructure.mongo;

import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.domain.models.Username;
import com.jeferro.ecommerce.users.users.domain.repositories.UsersRepository;
import com.jeferro.ecommerce.users.users.infrastructure.mongo.daos.UsersMongoDao;
import com.jeferro.ecommerce.users.users.infrastructure.mongo.mappers.UsersMongoMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersMongoRepository implements UsersRepository {

  private final UsersMongoMapper usersMongoMapper;

  private final UsersMongoDao usersMongoDao;

  @Override
  public Optional<User> findById(Username username) {
    var usernameDto = usersMongoMapper.toDTO(username);

    return usersMongoDao.findById(usernameDto).map(usersMongoMapper::toDomain);
  }
}
