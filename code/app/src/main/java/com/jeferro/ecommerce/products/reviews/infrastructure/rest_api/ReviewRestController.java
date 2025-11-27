package com.jeferro.ecommerce.products.reviews.infrastructure.rest_api;

import com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.dtos.CreateReviewInputRestDTO;
import com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.dtos.ReviewListRestDTO;
import com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.dtos.ReviewOrderRestDTO;
import com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.dtos.ReviewRestDTO;
import com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.dtos.UpdateReviewInputRestDTO;
import com.jeferro.ecommerce.products.reviews.infrastructure.rest_api.mappers.ReviewRestMapper;
import com.jeferro.shared.ddd.application.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewRestController implements ReviewsApi {

  private final ReviewRestMapper reviewRestMapper;

  private final UseCaseBus useCaseBus;

  @Override
  public ReviewListRestDTO searchReviews(
      Integer pageNumber,
      Integer pageSize,
      ReviewOrderRestDTO order,
      Boolean ascending,
      String entityId) {
    var params =
        reviewRestMapper.toSearchProductsParams(pageNumber, pageSize, order, ascending, entityId);

    var reviews = useCaseBus.execute(params);

    return reviewRestMapper.toSummaryListDTO(reviews);
  }

  @Override
  public ReviewRestDTO createReview(CreateReviewInputRestDTO inputRestDTO) {
    var params = reviewRestMapper.toCreateReviewParams(inputRestDTO);

    var reviews = useCaseBus.execute(params);

    return reviewRestMapper.toDTO(reviews);
  }

  @Override
  public ReviewRestDTO getReview(String reviewId) {
    var params = reviewRestMapper.toGetReviewParams(reviewId);

    var reviews = useCaseBus.execute(params);

    return reviewRestMapper.toDTO(reviews);
  }

  @Override
  public ReviewRestDTO updateReview(String reviewId, UpdateReviewInputRestDTO inputRestDTO) {
    var params = reviewRestMapper.toUpdateReviewParams(reviewId, inputRestDTO);

    var review = useCaseBus.execute(params);

    return reviewRestMapper.toDTO(review);
  }

  @Override
  public ReviewRestDTO deleteReview(String reviewId) {
    var params = reviewRestMapper.toDeleteReviewParams(reviewId);

    var review = useCaseBus.execute(params);

    return reviewRestMapper.toDTO(review);
  }
}
