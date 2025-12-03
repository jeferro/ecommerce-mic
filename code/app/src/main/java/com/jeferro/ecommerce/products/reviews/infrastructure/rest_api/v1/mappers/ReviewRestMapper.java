package com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.v1.mappers;

import com.jeferro.ecommerce.products.reviews.application.params.*;
import com.jeferro.ecommerce.products.reviews.domain.models.Review;
import com.jeferro.ecommerce.products.reviews.domain.models.ReviewId;
import com.jeferro.ecommerce.products.reviews.domain.models.criteria.ReviewCriteria;
import com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.v1.dtos.*;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.mappers.AggregatePrimaryMapper;
import com.jeferro.shared.mappers.MapstructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapstructConfig.class)
public abstract class ReviewRestMapper
    extends AggregatePrimaryMapper<Review, ReviewId, ReviewRestDTO> {

  public abstract ReviewListRestDTO toSummaryListDTO(PaginatedList<Review> reviews);

  public SearchReviewParams toSearchProductsParams(
      Integer pageNumber,
      Integer pageSize,
      ReviewOrderRestDTO order,
      Boolean ascending,
      String entityId) {
    var criteria = toReviewCriteria(pageNumber, pageSize, order, ascending, entityId);

    return new SearchReviewParams(criteria);
  }

  protected abstract ReviewCriteria toReviewCriteria(
      Integer pageNumber,
      Integer pageSize,
      ReviewOrderRestDTO order,
      Boolean ascending,
      String entityId);

  public abstract CreateReviewParams toCreateReviewParams(CreateReviewInputRestDTO inputRestDTO);

  public abstract GetReviewParams toGetReviewParams(String reviewId);

  public abstract UpdateReviewParams toUpdateReviewParams(
      String reviewId, UpdateReviewInputRestDTO inputRestDTO);

  public abstract DeleteReviewParams toDeleteReviewParams(String reviewId);
}
