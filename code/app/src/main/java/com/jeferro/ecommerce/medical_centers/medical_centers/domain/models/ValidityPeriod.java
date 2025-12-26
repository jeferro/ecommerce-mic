package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ValidityPeriod extends ValueObject {

  private final LocalDate startDate;
  private final LocalDate endDate;

  public ValidityPeriod(LocalDate startDate, LocalDate endDate) {
    ValueValidator.ensureNotNull(startDate, "startDate");

    this.startDate = startDate;
    this.endDate = endDate;
  }

  public static ValidityPeriod createWithCurrentDate(LocalDate endDate) {
    return new ValidityPeriod(LocalDate.now(), endDate);
  }
}

