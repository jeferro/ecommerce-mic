package com.jeferro.products.reviews.application;

import com.jeferro.products.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.products.reviews.domain.models.Review;
import com.jeferro.products.reviews.domain.models.ReviewCriteria;
import com.jeferro.products.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.jeferro.products.shared.application.Roles.ADMIN;

@Component
@RequiredArgsConstructor
public class DeleteAllReviewsOfEntityIdUseCase extends UseCase<DeleteAllReviewsOfEntityIdParams, Void> {

    private final ReviewsRepository reviewsRepository;

    private final EventBus eventBus;

    @Override
    public Set<String> getMandatoryUserRoles() {
        return Set.of(ADMIN);
    }

    @Override
    public Void execute(Auth auth, DeleteAllReviewsOfEntityIdParams params) {
        var entityId = params.getEntityId();

        var criteria = ReviewCriteria.byEntityId(entityId);
        var reviews = reviewsRepository.findAll(criteria);

        while(reviews.isNotEmpty()){
            reviews.forEach(Review::deleteBySystem);

            reviewsRepository.deleteAll(reviews);

            eventBus.sendAll(reviews);

            criteria.nextPage();
            reviews = reviewsRepository.findAll(criteria);
        }

        return null;
    }
}
