package com.jeferro.products.reviews.reviews.domain.models;

import com.jeferro.products.products.products.domain.models.ProductCode;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class ProductReviewId extends StringIdentifier {

    private static final String SEPARATOR = "::";

    private final String username;

    private final ProductCode productCode;

    public ProductReviewId(String value) {
        super(value);

        var split = value.split(SEPARATOR);

        this.username = split[0];
        this.productCode = new ProductCode(split[1]);
    }

    private ProductReviewId(String username, ProductCode productCode) {
        super(username + SEPARATOR + productCode);

        this.username = username;
        this.productCode = productCode;
    }

    public static ProductReviewId createOf(Auth auth, ProductCode productCode) {
        ValueValidationUtils.isNotNull(auth, "auth", ProductReview.class);
        ValueValidationUtils.isNotNull(productCode, "productCode", ProductReview.class);

        String username = auth.username();

        return new ProductReviewId(username, productCode);
    }
}
