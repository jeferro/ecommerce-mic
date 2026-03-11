package com.jeferro.ecommerce.users.users.domain.repositories;

import com.jeferro.ecommerce.shared.domain.repositories.FakeRepository;
import com.jeferro.ecommerce.users.users.domain.models.User;
import com.jeferro.ecommerce.users.users.domain.models.UserMother;
import com.jeferro.ecommerce.users.users.domain.models.Username;

public class UsersFakeRepository extends FakeRepository<User, Username>
    implements UsersRepository {

  public UsersFakeRepository() {
    var john = UserMother.john();
    save(john);

    var james = UserMother.james();
    save(james);

    var emily = UserMother.emily();
    save(emily);
  }
}
