package com.jeferro.ecommerce.products.reviews.domain.models;

import com.jeferro.ecommerce.products.reviews.domain.events.ReviewCreated;
import com.jeferro.ecommerce.products.reviews.domain.events.ReviewDeleted;
import com.jeferro.ecommerce.products.reviews.domain.events.ReviewUpdated;
import com.jeferro.ecommerce.products.reviews.domain.exceptions.ReviewDoesNotBelongUserException;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import java.util.Locale;
import lombok.Getter;

@Getter
public class Review extends AggregateRoot<ReviewId> {

  private Locale locale;

  private String comment;

  public Review(ReviewId id, String comment, Locale locale, int version, Metadata metadata) {
    super(id, version, metadata);

    this.locale = locale;
    this.comment = comment;
  }

  public static Review createOf(EntityId entityId, String comment, Auth auth) {
    ValueValidator.isNotNull(entityId, "entityId");
    ValueValidator.isNotNull(comment, "comment");
    ValueValidator.isNotNull(auth, "auth");

    var reviewId = ReviewId.createOf(entityId, auth);
    var review = new Review(reviewId, comment, auth.getLocale(), 1, null);

    var event = ReviewCreated.create(review);
    review.record(event);

    return review;
  }

  public void update(String comment, Auth auth) {
    ValueValidator.isNotNull(comment, "comment");
    ValueValidator.isNotNull(locale, "locale");

    this.comment = comment;
    this.locale = auth.getLocale();

    var event = ReviewUpdated.create(this);
    record(event);
  }

  public void deleteByUser() {
    var event = ReviewDeleted.create(this);
    record(event);
  }

  public void deleteBySystem() {
    var event = ReviewDeleted.create(this);
    record(event);
  }

  public String getUsername() {
    return id.getUsername();
  }

  public EntityId getEntityId() {
    return id.getEntityId();
  }

  public String getDomain() {
    return id.getEntityId().getDomain();
  }

  public void ensureReviewBelongsToUser(Auth auth) {
    if (auth.getUsername().equals(getUsername())) {
      return;
    }

    throw ReviewDoesNotBelongUserException.belongsToOtherUser(getId(), auth);
  }
}
