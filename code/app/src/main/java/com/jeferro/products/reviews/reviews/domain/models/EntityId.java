package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.shared.ddd.domain.exceptions.ValueValidationException;
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

        if( split.length != 2 ) {
            throw ValueValidationException.createOfMessage("Incorrect format " + value);
        }

        this.domain = split[0];
        this.id = split[1];
    }

    private EntityId(String domain, String id) {
        super(domain + SEPARATOR + id);

	    this.domain = domain;
	    this.id = id;
    }

    public static EntityId createOf(String domain, String id) {
        ValueValidationUtils.isNotNull(domain, "domain");
        ValueValidationUtils.isNotNull(id, "id");

        return new EntityId(domain, id);
    }
}
