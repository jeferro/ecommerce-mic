package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.GetReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.context.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class GetReviewUseCase extends UseCase<GetReviewParams, Review> {

    private final ReviewsRepository reviewsRepository;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public Review execute(Context context, GetReviewParams params) {
        var reviewId = params.getReviewId();

        return reviewsRepository.findByIdOrError(reviewId);
    }
}
