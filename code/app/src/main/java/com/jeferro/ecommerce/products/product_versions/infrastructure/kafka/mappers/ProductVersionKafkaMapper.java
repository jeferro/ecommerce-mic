package com.jeferro.ecommerce.products.product_versions.infrastructure.kafka.mappers;

import com.jeferro.ecommerce.products.product_versions.domain.events.*;
import com.jeferro.ecommerce.products.product_versions.infrastructure.kafka.dtos.*;
import com.jeferro.shared.mappers.EventMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public abstract class ProductVersionKafkaMapper extends EventMapper<ProductVersionEvent> {

  public Object toDTO(ProductVersionEvent event) {
    return switch (event) {
      case ProductVersionCreated productVersionCreated -> toDTO(productVersionCreated);
      case ProductVersionUpdated productVersionUpdated -> toDTO(productVersionUpdated);
      case ProductVersionPublished productVersionPublished -> toDTO(productVersionPublished);
      case ProductVersionUnpublished productVersionUnpublished -> toDTO(productVersionUnpublished);
      case ProductVersionDeleted productVersionDeleted -> toDTO(productVersionDeleted);

      default -> throw new IllegalStateException("Unexpected value: " + event);
    };
  }

  @Mapping(target = "entityBuilder", ignore = true)
  protected abstract ProductCreatedV1AvroDTO toDTO(ProductVersionCreated entity);

  @Mapping(target = "entityBuilder", ignore = true)
  protected abstract ProductUpdatedV1AvroDTO toDTO(ProductVersionUpdated entity);

  @Mapping(target = "entityBuilder", ignore = true)
  protected abstract ProductPublishedV1AvroDTO toDTO(ProductVersionPublished entity);

  @Mapping(target = "entityBuilder", ignore = true)
  protected abstract ProductUnpublishedV1AvroDTO toDTO(ProductVersionUnpublished entity);

  @Mapping(target = "entityBuilder", ignore = true)
  protected abstract ProductDeletedV1AvroDTO toDTO(ProductVersionDeleted entity);
}
