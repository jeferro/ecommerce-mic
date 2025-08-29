package com.jeferro.products.reviews.reviews.infrastructure.mongo.mappers;

import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.models.ReviewId;
import com.jeferro.products.reviews.reviews.infrastructure.mongo.dtos.ReviewMongoDTO;
import com.jeferro.shared.mappers.AggregateMongoMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapstructConfig.class)
public abstract class ReviewMongoMapper extends AggregateMongoMapper<Review, ReviewId, ReviewMongoDTO> {

    public static final ReviewMongoMapper INSTANCE = Mappers.getMapper(ReviewMongoMapper.class);
}
