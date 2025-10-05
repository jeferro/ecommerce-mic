package com.jeferro.products.products.infrastructure.mongo.dtos;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import com.jeferro.shared.auth.infrastructure.mongo.dtos.MetadataMongoDTO;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "products")
public class ProductVersionSummaryMongoDTO extends AuditedMongoDTO {

  public static final List<String> FIELDS = List.of("name", "status");

  private final String id;

  private final Map<String, String> name;

  private final ProductStatusMongoDTO status;

  public ProductVersionSummaryMongoDTO(
      String id,
      Map<String, String> name,
      ProductStatusMongoDTO status,
      MetadataMongoDTO metadata) {
    super(metadata);
    this.id = id;
    this.name = name;
    this.status = status;
  }
}
