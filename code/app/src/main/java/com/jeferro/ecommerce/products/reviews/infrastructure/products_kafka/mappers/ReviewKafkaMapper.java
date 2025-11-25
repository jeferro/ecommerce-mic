package com.jeferro.ecommerce.products.reviews.infrastructure.products_kafka.mappers;

import com.jeferro.ecommerce.products.product_versions.infrastructure.kafka.dtos.ProductVersionV1AvroDTO;
import com.jeferro.ecommerce.products.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.ecommerce.products.reviews.domain.events.ReviewEvent;
import com.jeferro.ecommerce.products.reviews.domain.models.EntityId;
import com.jeferro.shared.mappers.EventMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ReviewKafkaMapper extends EventMapper<ReviewEvent> {

  public static final ReviewKafkaMapper INSTANCE = Mappers.getMapper(ReviewKafkaMapper.class);

  @Mapping(target = "entityId", expression = "java(toDomain(domain, entity.getId()))")
  public abstract DeleteAllReviewsOfEntityIdParams toDeleteAllReviewsOfEntityIdParams(String domain, ProductVersionV1AvroDTO entity);

  public EntityId toDomain(String domain, String id) {
    return EntityId.createOf(domain, id);
  }
}
