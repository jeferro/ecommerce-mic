package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.services.ValueValidator;

public class MedicalCenterCode extends StringIdentifier {

  public MedicalCenterCode(String value) {
    super(value);
    ValueValidator.ensure(
        () -> value.matches("^[a-zA-Z0-9]+$"),
        "code must be alphanumeric");
  }

  public static MedicalCenterCode createOf(ParametricValueId provinceCode, long sequentialNumber) {
    ValueValidator.ensureNotNull(provinceCode, "provinceCode");
    ValueValidator.ensurePositive((int) sequentialNumber, "sequentialNumber");

    var codeValue = provinceCode.toString() + String.format("%06d", sequentialNumber);

    return new MedicalCenterCode(codeValue);
  }
}


