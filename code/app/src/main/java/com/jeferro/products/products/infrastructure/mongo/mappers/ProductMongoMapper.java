package com.jeferro.products.products.infrastructure.mongo.mappers;

import com.jeferro.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.domain.models.ProductVersionId;
import com.jeferro.products.products.domain.models.ProductVersionSummary;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionMongoDTO;
import com.jeferro.products.products.infrastructure.mongo.dtos.ProductVersionSummaryMongoDTO;
import com.jeferro.shared.mappers.AggregateMongoMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductMongoMapper
    extends AggregateMongoMapper<ProductVersion, ProductVersionId, ProductVersionMongoDTO> {

  public static final ProductMongoMapper INSTANCE = Mappers.getMapper(ProductMongoMapper.class);

  public abstract ProductVersionSummary toDomain(ProductVersionSummaryMongoDTO dto);

  public List<ProductVersionSummary> toDomainSummary(List<ProductVersionSummaryMongoDTO> elements) {
    return elements.stream().map(this::toDomain).toList();
  }
}
