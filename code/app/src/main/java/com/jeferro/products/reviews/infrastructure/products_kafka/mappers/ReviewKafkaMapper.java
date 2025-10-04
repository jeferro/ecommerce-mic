package com.jeferro.products.reviews.infrastructure.products_kafka.mappers;

import com.jeferro.products.reviews.domain.events.ReviewEvent;
import com.jeferro.products.reviews.domain.models.EntityId;
import com.jeferro.shared.mappers.EventMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ReviewKafkaMapper extends EventMapper<ReviewEvent> {

  public static final ReviewKafkaMapper INSTANCE = Mappers.getMapper(ReviewKafkaMapper.class);

  public EntityId toDomain(String domain, String id) {
    return EntityId.createOf(domain, id);
  }
}
