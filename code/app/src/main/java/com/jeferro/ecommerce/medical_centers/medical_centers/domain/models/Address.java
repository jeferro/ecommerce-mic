package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

@Getter
public class Address extends ValueObject {

  private static final String UNKNOWN_ADDRESS_TYPE = "25";

  private final ParametricValueId streetType;
  private final String streetName;
  private final String number;
  private final String floor;
  private final String door;
  private final String postalCode;
  private final String city;
  private final ParametricValueId province;
  private final ParametricValueId country;

  public Address(ParametricValueId streetType,
                 String streetName,
                 String number,
                 String floor,
                 String door,
                 String postalCode,
                 String city,
                 ParametricValueId province,
                 ParametricValueId country) {
    ValueValidator.ensureNotNull(streetType, "streetType");

    var isUnknownAddress = UNKNOWN_ADDRESS_TYPE.equals(streetType.toString());
    if (!isUnknownAddress) {
      ValueValidator.ensureNotBlank(streetName, "streetName");
    }

    this.streetType = streetType;
    this.streetName = streetName;
    this.number = number;
    this.floor = floor;
    this.door = door;
    this.postalCode = postalCode;
    this.city = city;
    this.province = province;
    this.country = country;
  }
}


