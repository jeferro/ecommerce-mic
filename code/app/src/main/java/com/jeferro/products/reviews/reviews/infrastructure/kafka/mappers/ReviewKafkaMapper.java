package com.jeferro.products.reviews.reviews.infrastructure.kafka.mappers;

import com.jeferro.products.generated.kafka.v1.dtos.ReviewCreatedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ReviewDeletedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ReviewUpdatedAvroDTO;
import com.jeferro.products.reviews.reviews.domain.events.ReviewCreated;
import com.jeferro.products.reviews.reviews.domain.events.ReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.events.ReviewEvent;
import com.jeferro.products.reviews.reviews.domain.events.ReviewUpdated;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ReviewKafkaMapper {

    public static final ReviewKafkaMapper INSTANCE = Mappers.getMapper(ReviewKafkaMapper.class);

    public Object toDTO(ReviewEvent event) {
        return switch (event) {
            case ReviewCreated reviewCreated -> toDTO(reviewCreated);
            case ReviewUpdated reviewUpdated -> toDTO(reviewUpdated);
            case ReviewDeleted reviewDeleted -> toDTO(reviewDeleted);

            default -> throw new IllegalStateException("Unexpected value: " + event);
        };
    }

    @Mapping(target = "entityIdBuilder", ignore = true)
    protected abstract ReviewCreatedAvroDTO toDTO(ReviewCreated entity);

    @Mapping(target = "entityIdBuilder", ignore = true)
    protected abstract ReviewUpdatedAvroDTO toDTO(ReviewUpdated entity);

    protected abstract ReviewDeletedAvroDTO toDTO(ReviewDeleted entity);
}
