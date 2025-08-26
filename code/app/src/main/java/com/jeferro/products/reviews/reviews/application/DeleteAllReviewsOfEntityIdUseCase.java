package com.jeferro.products.reviews.reviews.application;

import com.jeferro.products.reviews.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.products.reviews.reviews.domain.models.Review;
import com.jeferro.products.reviews.reviews.domain.repositories.ReviewsRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.context.Context;
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
    public Void execute(Context context, DeleteAllReviewsOfEntityIdParams params) {
        var entityId = params.getDomain();

        var reviews = reviewsRepository.findAllByProductCode(entityId);

        if (reviews.isEmpty()) {
            return null;
        }

        reviews.forEach(Review::deleteBySystem);

        reviewsRepository.deleteAll(reviews);

        eventBus.sendAll(reviews);

        return null;
    }
}
