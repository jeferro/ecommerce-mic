package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import lombok.Getter;

import java.util.List;

@Getter
public class BillingIndicators extends ValueObject {

  private final ParametricValueId changeReasons;
  private final Boolean ownershipChange;
  private final Boolean pendingAssistancePayment;
  private final Boolean notApplicablePreviousIndicator;
  private final Reconciliation reconciliation;
  private final List<BillingIndicatorChange> changeHistory;

  public BillingIndicators(ParametricValueId changeReasons,
                            Boolean ownershipChange,
                            Boolean pendingAssistancePayment,
                            Boolean notApplicablePreviousIndicator,
                            Reconciliation reconciliation,
                            List<BillingIndicatorChange> changeHistory) {
    this.changeReasons = changeReasons;
    this.ownershipChange = ownershipChange;
    this.pendingAssistancePayment = pendingAssistancePayment;
    this.notApplicablePreviousIndicator = notApplicablePreviousIndicator;
    this.reconciliation = reconciliation;
    this.changeHistory = changeHistory;
  }
}


