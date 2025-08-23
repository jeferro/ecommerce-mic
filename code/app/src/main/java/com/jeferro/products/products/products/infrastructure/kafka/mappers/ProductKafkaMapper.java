package com.jeferro.products.products.products.infrastructure.kafka.mappers;

import com.jeferro.products.generated.kafka.v1.dtos.*;
import com.jeferro.products.products.products.domain.events.*;
import com.jeferro.products.products.products.domain.models.ProductCode;
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

    public abstract ProductCode toDomain(String value);
}
