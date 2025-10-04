package com.jeferro.products.users.domain.repositories;

import com.jeferro.products.shared.domain.repositories.InMemoryRepository;
import com.jeferro.products.users.domain.models.User;
import com.jeferro.products.users.domain.models.UserMother;
import com.jeferro.products.users.domain.models.Username;

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
