package com.jeferro.ecommerce.users.users.domain.repositories;

import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.domain.models.Username;
import java.util.Optional;

public interface UsersRepository {

  Optional<User> findById(Username username);
}
