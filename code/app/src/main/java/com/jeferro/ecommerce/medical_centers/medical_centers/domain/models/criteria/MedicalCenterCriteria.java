package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.criteria;

import static com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.criteria.MedicalCenterOrder.CODE;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.status.MedicalCenterStatus;
import com.jeferro.shared.ddd.domain.models.filter.DomainCriteria;
import lombok.Getter;

@Getter
public class MedicalCenterCriteria extends DomainCriteria<MedicalCenterOrder> {

  private final MedicalCenterId id;
  private final MedicalCenterStatus status;
  private final String holderId;

  public MedicalCenterCriteria(Integer pageNumber,
                                Integer pageSize,
                                MedicalCenterOrder order,
                                Boolean ascending,
                                MedicalCenterId id,
                                MedicalCenterStatus status,
                                String holderId) {
    super(pageNumber, pageSize, order, ascending);

    this.id = id;
    this.status = status;
    this.holderId = holderId;
  }

  public static MedicalCenterCriteria allPage() {
    return new MedicalCenterCriteria(null, null, CODE, null, null, null, null);
  }

  public static MedicalCenterCriteria byId(MedicalCenterId id) {
    return new MedicalCenterCriteria(null, null, CODE, null, id, null, null);
  }

  public static MedicalCenterCriteria byStatus(MedicalCenterStatus status) {
    return new MedicalCenterCriteria(null, null, CODE, null, null, status, null);
  }

  public static MedicalCenterCriteria byHolderId(String holderId) {
    return new MedicalCenterCriteria(null, null, CODE, null, null, null, holderId);
  }

  public boolean hasId() {
    return id != null;
  }

  public boolean hasStatus() {
    return status != null;
  }

  public boolean hasHolderId() {
    return holderId != null;
  }
}

