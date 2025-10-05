package com.jeferro.products.users.infrastructure.mongo.dtos;

import java.time.Instant;
import java.util.Set;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "users")
public class UserMongoDTO extends AuditedMongoDTO {

  private final String id;

  private final String encodedPassword;

  private final Set<String> roles;

  public UserMongoDTO(Instant createdAt,
      String createdBy,
      Instant updatedAt,
      String updatedBy,
      String id,
      String encodedPassword,
      Set<String> roles) {
    super(createdAt,
        createdBy,
        updatedAt,
        updatedBy);
    this.id = id;
    this.encodedPassword = encodedPassword;
    this.roles = roles;
  }
}
