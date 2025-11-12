package com.jeferro.ecommerce.users.users.domain.repositories;

import com.jeferro.ecommerce.shared.domain.repositories.InMemoryRepository;
import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.domain.models.UserMother;
import com.jeferro.ecommerce.users.users.domain.models.Username;

public class UsersInMemoryRepository extends InMemoryRepository<User, Username>
    implements UsersRepository {

  public UsersInMemoryRepository() {
    var john = UserMother.john();
    data.put(john.getId(), john);

    var james = UserMother.james();
    data.put(james.getId(), james);

    var emily = UserMother.emily();
    data.put(emily.getId(), emily);
  }
}
