package com.jeferro.products.reviews.domain.models;

import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class ReviewId extends StringIdentifier {

    private static final String SEPARATOR = "::";

    private final String username;

    private final EntityId entityId;

    public ReviewId(String value) {
        super(value);

	    var split = value.split(SEPARATOR);

        if( split.length != 3 ) {
            throw ValueValidationException.createOfMessage("Incorrect format " + value);
        }

        this.username = split[0];
        this.entityId = EntityId.createOf(split[1], split[2]);
    }

    private ReviewId(String username, EntityId entityId) {
        super(username + SEPARATOR + entityId);

        this.username = username;
	    this.entityId = entityId;
    }

    public static ReviewId createOf(EntityId entityId, Auth auth) {
        ValueValidator.isNotNull(entityId, "entityId");
        ValueValidator.isNotNull(auth, "auth");

        String username = auth.getUsername();

        return new ReviewId(username, entityId);
    }
}
