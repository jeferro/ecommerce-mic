package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BillingIndicatorChange extends ValueObject {

  private final LocalDate startDate;
  private final LocalDate endDate;
  private final ParametricValueId changeReason;
  private final Boolean ownershipChange;
  private final Boolean pendingAssistancePayment;
  private final Boolean notApplicablePreviousIndicator;

  public BillingIndicatorChange(LocalDate startDate,
                                LocalDate endDate,
                                ParametricValueId changeReason,
                                Boolean ownershipChange,
                                Boolean pendingAssistancePayment,
                                Boolean notApplicablePreviousIndicator) {
    ValueValidator.ensureNotNull(startDate, "startDate");

    this.startDate = startDate;
    this.endDate = endDate;
    this.changeReason = changeReason;
    this.ownershipChange = ownershipChange;
    this.pendingAssistancePayment = pendingAssistancePayment;
    this.notApplicablePreviousIndicator = notApplicablePreviousIndicator;
  }
}


