package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.util.List;

import static com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.CenterClassType.EXTRASHOSPITAL;
import static com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.CenterClassType.HOSPITAL;

@Getter
public class CenterClass extends ValueObject {

  private final CenterClassType type;
  private final List<ParametricValueId> hospitalClasses;
  private final List<ParametricValueId> extrahospitalClasses;

  public CenterClass(CenterClassType type,
                     List<ParametricValueId> hospitalClasses,
                     List<ParametricValueId> extrahospitalClasses) {
    ValueValidator.ensureNotNull(type, "type");

    if (HOSPITAL.equals(type)) {
      ValueValidator.ensureNotEmpty(hospitalClasses, "hospitalClasses");
      ValueValidator.ensure(
          () -> extrahospitalClasses == null || extrahospitalClasses.isEmpty(),
          "extrahospitalClasses must be empty when type is HOSPITAL");
    } else if (EXTRASHOSPITAL.equals(type)) {
      ValueValidator.ensureNotEmpty(extrahospitalClasses, "extrahospitalClasses");
      ValueValidator.ensure(
          () -> hospitalClasses == null || hospitalClasses.isEmpty(),
          "hospitalClasses must be empty when type is EXTRASHOSPITAL");
    }

    this.type = type;
    this.hospitalClasses = hospitalClasses;
    this.extrahospitalClasses = extrahospitalClasses;
  }
}

