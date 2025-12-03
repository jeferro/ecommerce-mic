package com.jeferro.ecommerce.products.reviews.infrastructure.products_kafka.v1.mappers;

import com.jeferro.ecommerce.products.product_versions.infrastructure.kafka.v1.dtos.*;
import com.jeferro.ecommerce.products.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.ecommerce.products.reviews.domain.events.ReviewEvent;
import com.jeferro.ecommerce.products.reviews.domain.models.EntityId;
import com.jeferro.shared.mappers.EventMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
public abstract class ReviewKafkaMapper extends EventMapper<ReviewEvent> {

  @Mapping(target = "entityId", expression = "java(toDomain(domain, entity.getId()))")
  public abstract DeleteAllReviewsOfEntityIdParams toDeleteAllReviewsOfEntityIdParams(String domain, ProductVersionV1AvroDTO entity);

  public EntityId toDomain(String domain, String id) {
    return EntityId.createOf(domain, id);
  }
}
