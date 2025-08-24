package com.jeferro.products.reviews.reviews.infrastructure.kafka.mappers;

import com.jeferro.products.generated.kafka.v1.dtos.ProductReviewCreatedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductReviewDeletedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductReviewUpdatedAvroDTO;
import com.jeferro.products.reviews.reviews.domain.events.ProductReviewCreated;
import com.jeferro.products.reviews.reviews.domain.events.ProductReviewDeleted;
import com.jeferro.products.reviews.reviews.domain.events.ProductReviewEvent;
import com.jeferro.products.reviews.reviews.domain.events.ProductReviewUpdated;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductReviewKafkaMapper {

    public static final ProductReviewKafkaMapper INSTANCE = Mappers.getMapper(ProductReviewKafkaMapper.class);

    public Object toDTO(ProductReviewEvent event) {
        return switch (event) {
            case ProductReviewCreated productReviewCreated -> toDTO(productReviewCreated);
            case ProductReviewUpdated productReviewUpdated -> toDTO(productReviewUpdated);
            case ProductReviewDeleted productReviewDeleted -> toDTO(productReviewDeleted);

            default -> throw new IllegalStateException("Unexpected value: " + event);
        };
    }

    protected abstract ProductReviewCreatedAvroDTO toDTO(ProductReviewCreated entity);

    protected abstract ProductReviewUpdatedAvroDTO toDTO(ProductReviewUpdated entity);

    protected abstract ProductReviewDeletedAvroDTO toDTO(ProductReviewDeleted entity);
}
