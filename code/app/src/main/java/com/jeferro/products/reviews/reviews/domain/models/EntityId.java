package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class EntityId extends StringIdentifier {

    private static final String SEPARATOR = "::";

    private final String domain;

    private final String id;

    public EntityId(String value) {
        super(value);

	    var split = value.split(SEPARATOR);

        this.domain = split[0];
        this.id = split[1];
    }

    private EntityId(String domain, String id) {
        super(domain + SEPARATOR + id);

	    this.domain = domain;
	    this.id = id;
    }

    public static EntityId createOf(String domain, String id) {
        ValueValidationUtils.isNotNull(domain, "domain", Review.class);
        ValueValidationUtils.isNotNull(id, "id", Review.class);

        return new EntityId(domain, id);
    }
}
