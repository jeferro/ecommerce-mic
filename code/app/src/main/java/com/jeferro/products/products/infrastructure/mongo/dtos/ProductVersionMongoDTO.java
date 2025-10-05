package com.jeferro.products.products.infrastructure.mongo.dtos;

import java.time.Instant;
import java.util.Map;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "products")
public class ProductVersionMongoDTO extends AuditedMongoDTO {

  private final String id;

  private final String code;

  private final Instant effectiveDate;

  private final Instant endEffectiveDate;

  private final String typeId;

  private final ProductStatusMongoDTO status;

  private final Map<String, String> name;

  public ProductVersionMongoDTO(Instant createdAt,
      String createdBy,
      Instant updatedAt,
      String updatedBy,
      String id,
      String code,
      Instant effectiveDate,
      Instant endEffectiveDate,
      String typeId,
      ProductStatusMongoDTO status,
      Map<String, String> name) {
    super(createdAt,
        createdBy,
        updatedAt,
        updatedBy);
    this.id = id;
    this.code = code;
    this.effectiveDate = effectiveDate;
    this.endEffectiveDate = endEffectiveDate;
    this.typeId = typeId;
    this.status = status;
    this.name = name;
  }
}
