package com.jeferro.products.products.products.application.params;

import com.jeferro.products.products.products.domain.models.ProductVersion;
import com.jeferro.products.products.products.domain.models.ProductVersionId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.utils.ValueValidationUtils;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import lombok.Getter;

@Getter
public class UpdateProductParams extends Params<ProductVersion> {

    private final ProductVersionId versionId;

    private final LocalizedField name;

    public UpdateProductParams(ProductVersionId versionId, LocalizedField name) {
        super();

        ValueValidationUtils.isNotNull(versionId, "id");
        ValueValidationUtils.isNotNull(name, "name");

        this.versionId = versionId;
        this.name = name;
    }
}
