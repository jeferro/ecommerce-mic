package com.jeferro.products.users.infrastructure.mongo.dtos;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import com.jeferro.shared.auth.infrastructure.mongo.dtos.MetadataMongoDTO;
import java.util.Set;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "users")
public class UserMongoDTO extends AuditedMongoDTO {

  private final String id;

  private final String encodedPassword;

  private final Set<String> roles;

  public UserMongoDTO(
      String id, String encodedPassword, Set<String> roles, MetadataMongoDTO metadata) {
    super(metadata);
    this.id = id;
    this.encodedPassword = encodedPassword;
    this.roles = roles;
  }
}
