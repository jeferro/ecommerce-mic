package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.SearchReviewParams;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.USER;

@Component
@RequiredArgsConstructor
public class SearchReviewUseCase extends UseCase<SearchReviewParams, PaginatedList<Review>> {

    private final ReviewsRepository reviewsRepository;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(USER);
    }

    @Override
    public PaginatedList<Review> execute(Auth auth, SearchReviewParams params) {
        var criteria = params.getCriteria();

        return reviewsRepository.findAll(criteria);
    }
}
