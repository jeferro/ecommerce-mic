package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class ReviewId extends StringIdentifier {

    private static final String SEPARATOR = "::";

    private final String username;

    private final EntityId entityId;

    public ReviewId(String value) {
        super(value);

	    var split = value.split(SEPARATOR);

        this.username = split[0];
        this.entityId = EntityId.createOf(split[1], split[2]);
    }

    private ReviewId(String username, EntityId entityId) {
        super(username + SEPARATOR + entityId);

        this.username = username;
	    this.entityId = entityId;
    }

    public static ReviewId createOf(EntityId entityId, Auth auth) {
        ValueValidationUtils.isNotNull(entityId, "entityId", Review.class);
        ValueValidationUtils.isNotNull(auth, "auth", Review.class);

        String username = auth.username();

        return new ReviewId(username, entityId);
    }
}
