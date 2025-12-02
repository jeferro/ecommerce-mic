package com.jeferro.ecommerce.products.product_versions.infrastructure.mongo.dtos;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import com.jeferro.shared.auth.infrastructure.mongo.dtos.MetadataMongoDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "product_versions")
public class ProductVersionSummaryMongoDTO extends AuditedMongoDTO {

  public static final List<String> FIELDS = List.of("name", "price", "discount", "status", "version", "metadata");

  private final String id;

  private final Map<String, String> name;

  private final BigDecimal price;

  private final BigDecimal discount;

  private final ProductStatusMongoDTO status;

  public ProductVersionSummaryMongoDTO(
          String id,
          Map<String, String> name,
          ProductStatusMongoDTO status,
          Long version,
          MetadataMongoDTO metadata, BigDecimal price, BigDecimal discount) {
    super(version, metadata);
    this.id = id;
    this.name = name;
    this.status = status;
    this.price = price;
    this.discount = discount;
  }
}
