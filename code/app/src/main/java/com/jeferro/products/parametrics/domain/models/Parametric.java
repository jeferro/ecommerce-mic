package com.jeferro.products.parametrics.domain.models;

import com.jeferro.products.parametrics.domain.exceptions.ParametricValueNotFoundException;
import com.jeferro.products.parametrics.domain.models.values.ParametricValue;
import com.jeferro.products.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.projection.Projection;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.List;
import lombok.Getter;

@Getter
public class Parametric extends Projection<ParametricId> {

  private final LocalizedField name;

  private final List<ParametricValue> values;

  public Parametric(ParametricId id, LocalizedField name, List<ParametricValue> values) {
    super(id);

    this.name = name;
    this.values = values;
  }

  public static Parametric createOf(
      ParametricId id, LocalizedField name, List<ParametricValue> values) {
    ValueValidator.isNotNull(name, "name");
    ValueValidator.isNotEmpty(values, "values");

    return new Parametric(id, name, values);
  }

  public boolean validate(ParametricValueId parametricValueId) {
    if (notContainsParametricValue(parametricValueId)) {
      throw ParametricValueNotFoundException.createOf(this, parametricValueId);
    }

    return true;
  }

  private boolean notContainsParametricValue(ParametricValueId parametricValueId) {
    return values.stream()
        .noneMatch(parametricValue -> parametricValue.hasSameId(parametricValueId));
  }
}
