package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class HealthAuthorization extends ValueObject {

  private static final int MAX_REGISTRATION_NUMBER_LENGTH = 10;

  private final ParametricValueId autonomousCommunity;
  private final String registrationNumber;

  public HealthAuthorization(ParametricValueId autonomousCommunity, String registrationNumber) {
    this.autonomousCommunity = autonomousCommunity;

    if (registrationNumber != null) {
      ValueValidator.ensure(
          () -> registrationNumber.length() <= MAX_REGISTRATION_NUMBER_LENGTH,
          "registrationNumber exceeds maximum length of " + MAX_REGISTRATION_NUMBER_LENGTH);
      ValueValidator.ensure(
          () -> registrationNumber.matches("^[a-zA-Z0-9]+$"),
          "registrationNumber must be alphanumeric");
      ValueValidator.ensure(
          () -> !registrationNumber.contains("\n"),
          "registrationNumber must be single line");
    }

    this.registrationNumber = registrationNumber;
  }
}

