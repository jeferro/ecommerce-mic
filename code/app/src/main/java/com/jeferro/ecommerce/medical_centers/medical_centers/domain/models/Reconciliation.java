package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Reconciliation extends ValueObject {

  private final boolean reconciles;
  private final ParametricValueId reconciliationMethod;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final ParametricValueId changeReason;
  private final Boolean ownershipChange;
  private final Boolean pendingAssistancePayment;
  private final Boolean notApplicablePreviousIndicator;

  public Reconciliation(boolean reconciles,
                         ParametricValueId reconciliationMethod,
                         LocalDate startDate,
                         LocalDate endDate,
                         ParametricValueId changeReason,
                         Boolean ownershipChange,
                         Boolean pendingAssistancePayment,
                         Boolean notApplicablePreviousIndicator) {
    this.reconciles = reconciles;

    if (reconciles) {
      ValueValidator.ensureNotNull(reconciliationMethod, "reconciliationMethod");
      var methodValue = reconciliationMethod.toString();
      ValueValidator.ensure(
          () -> methodValue.equals("Por portal") || methodValue.equals("Por ficheros") || methodValue.equals("Otros"),
          "reconciliationMethod must be one of: Por portal, Por ficheros, Otros");
    }

    this.reconciliationMethod = reconciliationMethod;
    this.startDate = startDate;
    this.endDate = endDate;
    this.changeReason = changeReason;
    this.ownershipChange = ownershipChange;
    this.pendingAssistancePayment = pendingAssistancePayment;
    this.notApplicablePreviousIndicator = notApplicablePreviousIndicator;
  }
}


