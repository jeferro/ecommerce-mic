package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;

public class MedicalCenterId extends StringIdentifier {

  public MedicalCenterId(String value) {
    super(value);
  }

  public static MedicalCenterId createOf(String value) {
    return new MedicalCenterId(value);
  }
}

