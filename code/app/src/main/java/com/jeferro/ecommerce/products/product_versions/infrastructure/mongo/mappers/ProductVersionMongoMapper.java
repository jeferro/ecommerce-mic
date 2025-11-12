package com.jeferro.ecommerce.products.product_versions.infrastructure.mongo.mappers;

import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersion;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionId;
import com.jeferro.ecommerce.products.product_versions.domain.models.ProductVersionSummary;
import com.jeferro.ecommerce.products.product_versions.infrastructure.mongo.dtos.ProductVersionMongoDTO;
import com.jeferro.ecommerce.products.product_versions.infrastructure.mongo.dtos.ProductVersionSummaryMongoDTO;
import com.jeferro.shared.mappers.AggregateSecondaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductVersionMongoMapper
    extends AggregateSecondaryMapper<ProductVersion, ProductVersionId, ProductVersionMongoDTO> {

  public static final ProductVersionMongoMapper INSTANCE = Mappers.getMapper(ProductVersionMongoMapper.class);

  public abstract ProductVersionSummary toDomain(ProductVersionSummaryMongoDTO dto);

  public List<ProductVersionSummary> toDomainSummary(List<ProductVersionSummaryMongoDTO> elements) {
    return elements.stream().map(this::toDomain).toList();
  }
}
