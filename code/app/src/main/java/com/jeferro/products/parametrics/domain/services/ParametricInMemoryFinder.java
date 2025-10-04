package com.jeferro.products.parametrics.domain.services;

import com.jeferro.products.parametrics.domain.models.Parametric;
import com.jeferro.products.parametrics.domain.models.ParametricId;
import com.jeferro.products.parametrics.domain.models.values.ParametricValue;
import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParametricInMemoryFinder implements ParametricFinder {

  private final List<Parametric> parametrics = List.of(mockProductType());

  @Override
  public Optional<Parametric> findById(ParametricId parametricId) {
    return parametrics.stream()
        .filter(parametric -> parametric.hasSameId(parametricId))
        .findFirst();
  }

  private Parametric mockProductType() {
    var values = new ArrayList<ParametricValue>();

    var fruitValue =
        new ParametricValue(
            new ParametricValueId("fruit"), LocalizedField.createOf("en-US", "Fruit"), null);
    values.add(fruitValue);

    var vegetablesValue =
        new ParametricValue(
            new ParametricValueId("vegetables"),
            LocalizedField.createOf("en-US", "Vegetables"),
            null);
    values.add(vegetablesValue);

    return new Parametric(
        new ParametricId("product-types"),
        LocalizedField.createOf("en-US", "Product Types"),
        values);
  }
}
