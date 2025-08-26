package com.jeferro.products.reviews.reviews.infrastructure.kafka.mappers;

import com.jeferro.products.generated.kafka.v1.dtos.ProductReviewCreatedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductReviewDeletedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductReviewUpdatedAvroDTO;
import com.jeferro.products.reviews.reviews.domain.events.ReviewCreated;
import com.jeferro.products.reviews.reviews.domain.events.ReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.events.ReviewEvent;
import com.jeferro.products.reviews.reviews.domain.events.ReviewUpdated;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductReviewKafkaMapper {

    public static final ProductReviewKafkaMapper INSTANCE = Mappers.getMapper(ProductReviewKafkaMapper.class);

    public Object toDTO(ReviewEvent event) {
        return switch (event) {
            case ReviewCreated reviewCreated -> toDTO(reviewCreated);
            case ReviewUpdated productReviewUpdated -> toDTO(productReviewUpdated);
            case ReviewDeleted reviewDeleted -> toDTO(reviewDeleted);

            default -> throw new IllegalStateException("Unexpected value: " + event);
        };
    }

    protected abstract ProductReviewCreatedAvroDTO toDTO(ReviewCreated entity);

    protected abstract ProductReviewUpdatedAvroDTO toDTO(ReviewUpdated entity);

    protected abstract ProductReviewDeletedAvroDTO toDTO(ReviewDeleted entity);
}
