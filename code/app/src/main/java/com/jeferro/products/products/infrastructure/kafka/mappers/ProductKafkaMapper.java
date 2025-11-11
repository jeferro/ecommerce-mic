package com.jeferro.products.products.infrastructure.kafka.mappers;

import com.jeferro.products.products.domain.events.ProductVersionCreated;
import com.jeferro.products.products.domain.events.ProductVersionDeleted;
import com.jeferro.products.products.domain.events.ProductVersionEvent;
import com.jeferro.products.products.domain.events.ProductVersionPublished;
import com.jeferro.products.products.domain.events.ProductVersionUnpublished;
import com.jeferro.products.products.domain.events.ProductVersionUpdated;
import com.jeferro.products.products.infrastructure.kafka.dtos.ProductCreatedV1AvroDTO;
import com.jeferro.products.products.infrastructure.kafka.dtos.ProductDeletedV1AvroDTO;
import com.jeferro.products.products.infrastructure.kafka.dtos.ProductPublishedV1AvroDTO;
import com.jeferro.products.products.infrastructure.kafka.dtos.ProductUnpublishedV1AvroDTO;
import com.jeferro.products.products.infrastructure.kafka.dtos.ProductUpdatedV1AvroDTO;
import com.jeferro.shared.mappers.EventMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductKafkaMapper extends EventMapper<ProductVersionEvent> {

  public static final ProductKafkaMapper INSTANCE = Mappers.getMapper(ProductKafkaMapper.class);

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
