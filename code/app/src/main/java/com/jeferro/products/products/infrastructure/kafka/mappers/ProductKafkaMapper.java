package com.jeferro.products.products.infrastructure.kafka.mappers;

import com.jeferro.products.generated.kafka.v1.dtos.ProductCreatedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductDeletedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductPublishedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductUnpublishedAvroDTO;
import com.jeferro.products.generated.kafka.v1.dtos.ProductUpdatedAvroDTO;
import com.jeferro.products.products.domain.events.ProductVersionCreated;
import com.jeferro.products.products.domain.events.ProductVersionDeleted;
import com.jeferro.products.products.domain.events.ProductVersionEvent;
import com.jeferro.products.products.domain.events.ProductVersionPublished;
import com.jeferro.products.products.domain.events.ProductVersionUnpublished;
import com.jeferro.products.products.domain.events.ProductVersionUpdated;
import com.jeferro.shared.mappers.EventMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
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

    protected abstract ProductCreatedAvroDTO toDTO(ProductVersionCreated entity);

    protected abstract ProductUpdatedAvroDTO toDTO(ProductVersionUpdated entity);

    protected abstract ProductPublishedAvroDTO toDTO(ProductVersionPublished entity);

    protected abstract ProductUnpublishedAvroDTO toDTO(ProductVersionUnpublished entity);

    protected abstract ProductDeletedAvroDTO toDTO(ProductVersionDeleted entity);
}
