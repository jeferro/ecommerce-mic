package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HolderHistoryEntry extends ValueObject {

  private final String holderCode;
  private final String holderName;
  private final LocalDate registrationDate;
  private final LocalDate deactivationDate;

  public HolderHistoryEntry(String holderCode,
                            String holderName,
                            LocalDate registrationDate,
                            LocalDate deactivationDate) {
    ValueValidator.ensureNotNull(holderCode, "holderCode");
    ValueValidator.ensureNotNull(holderName, "holderName");
    ValueValidator.ensureNotNull(registrationDate, "registrationDate");

    this.holderCode = holderCode;
    this.holderName = holderName;
    this.registrationDate = registrationDate;
    this.deactivationDate = deactivationDate;
  }
}

