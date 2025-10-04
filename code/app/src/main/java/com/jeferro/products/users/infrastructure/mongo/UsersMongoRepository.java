package com.jeferro.products.users.infrastructure.mongo;

import com.jeferro.products.users.domain.models.User;
import com.jeferro.products.users.domain.models.Username;
import com.jeferro.products.users.domain.repositories.UsersRepository;
import com.jeferro.products.users.infrastructure.mongo.daos.UsersMongoDao;
import com.jeferro.products.users.infrastructure.mongo.mappers.UsersMongoMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersMongoRepository implements UsersRepository {

  private final UsersMongoMapper usersMongoMapper = UsersMongoMapper.INSTANCE;

  private final UsersMongoDao usersMongoDao;

  @Override
  public Optional<User> findById(Username username) {
    var usernameDto = usersMongoMapper.toDTO(username);

    return usersMongoDao.findById(usernameDto).map(usersMongoMapper::toDomain);
  }
}
