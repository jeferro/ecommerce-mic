package com.jeferro.products.products.infrastructure.mongo.dtos;

import java.time.Instant;
import java.util.Map;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "products")
public class ProductVersionSummaryMongoDTO extends AuditedMongoDTO {

  private final String id;

  private final Map<String, String> name;

  private final ProductStatusMongoDTO status;

  public ProductVersionSummaryMongoDTO(Instant createdAt,
      String createdBy,
      Instant updatedAt,
      String updatedBy,
      String id,
      Map<String, String> name,
      ProductStatusMongoDTO status) {
    super(createdAt,
        createdBy,
        updatedAt,
        updatedBy);
    this.id = id;
    this.name = name;
    this.status = status;
  }
}
