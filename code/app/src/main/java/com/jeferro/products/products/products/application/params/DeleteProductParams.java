package com.jeferro.products.products.products.application.params;

import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import lombok.Getter;

@Getter
public class DeleteProductParams extends Params<ProductVersion> {

    private final ProductVersionId versionId;

    public DeleteProductParams(ProductVersionId versionId) {
        super();

        ValueValidationUtils.isNotNull(versionId, "id");

        this.versionId = versionId;
    }
}
