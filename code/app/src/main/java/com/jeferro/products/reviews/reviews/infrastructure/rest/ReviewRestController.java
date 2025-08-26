package com.jeferro.products.reviews.reviews.infrastructure.rest;

import com.jeferro.products.generated.rest.v1.apis.ReviewsApi;
import com.jeferro.products.generated.rest.v1.dtos.CreateReviewInputRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ReviewListRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ReviewOrderRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.ReviewRestDTO;
import com.jeferro.products.generated.rest.v1.dtos.UpdateReviewInputRestDTO;
import com.jeferro.products.reviews.reviews.infrastructure.rest.mappers.ReviewRestMapper;
import com.jeferro.shared.ddd.application.bus.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewRestController implements ReviewsApi {

    private final ReviewRestMapper reviewRestMapper = ReviewRestMapper.INSTANCE;

    private final UseCaseBus useCaseBus;

    @Override
    public ReviewListRestDTO searchReviews(Integer pageNumber, Integer pageSize, ReviewOrderRestDTO order, Boolean ascending, String domain,
        String id) {
        var params = reviewRestMapper.toSearchProductsParams(pageNumber,
            pageSize,
            order,
            ascending,
            domain,
            id);

        var reviews = useCaseBus.execute(params);

        return reviewRestMapper.toSummaryListDTO(reviews);
    }

    @Override
    public ReviewRestDTO createReview(CreateReviewInputRestDTO inputRestDTO) {
        var params = reviewRestMapper.toCreateProductReviewParams(inputRestDTO);

        var reviews = useCaseBus.execute(params);

        return reviewRestMapper.toDTO(reviews);
    }

    @Override
    public ReviewRestDTO getReview(String reviewId) {
        var params = reviewRestMapper.toGetProductReviewParams(reviewId);

        var reviews = useCaseBus.execute(params);

        return reviewRestMapper.toDTO(reviews);
    }

    @Override
    public ReviewRestDTO updateReview(String reviewId, UpdateReviewInputRestDTO inputRestDTO) {
        var params = reviewRestMapper.toUpdateProductReviewParams(reviewId, inputRestDTO);

        var review = useCaseBus.execute(params);

        return reviewRestMapper.toDTO(review);
    }

    @Override
    public ReviewRestDTO deleteReview(String reviewId) {
        var params = reviewRestMapper.toDeleteProductReviewParams(reviewId);

        var review = useCaseBus.execute(params);

        return reviewRestMapper.toDTO(review);
    }
}
