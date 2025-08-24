package com.jeferro.products.reviews.reviews.infrastructure.mongo.mappers;

import com.jeferro.products.reviews.reviews.domain.models.ProductReview;
import com.jeferro.products.reviews.reviews.domain.models.ProductReviewId;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.dtos.ProductReviewMongoDTO;
import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.mappers.AggregateMongoMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ProductReviewMongoMapper extends AggregateMongoMapper<ProductReview, ProductReviewId, ProductReviewMongoDTO> {

    public static final ProductReviewMongoMapper INSTANCE = Mappers.getMapper(ProductReviewMongoMapper.class);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    public abstract ProductReview toDomain(ProductReviewMongoDTO dto);

    public abstract ProductReviewMongoDTO toDTO(ProductReview entity);

    public String toDTO(ProductCode productCode) {
        return productCode.getValue();
    }
}
