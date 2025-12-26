package com.jeferro.ecommerce.medical_centers.medical_centers.application.params;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.*;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.application.params.Params;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CreateMedicalCenterParams extends Params<MedicalCenter> {

  private final String holderId;
  private final String groupCode;
  private final ParametricValueId treatment;
  private final String name;
  private final LocalDate registrationDate;
  private final Address address;
  private final List<String> phones;
  private final HealthAuthorization healthAuthorization;
  private final CenterClass centerClass;
  private final Contact portalContact;
  private final Contact communicationContact;
  private final Contact medicalDirectionContact;
  private final List<Contact> billingContacts;
  private final List<Contact> authorizationContacts;
  private final Boolean publishableInGuides;
  private final Boolean onlineAppointment;
  private final ValidityPeriod validityPeriod;
  private final BillingIndicators billingIndicators;
  private final BankAccount bankAccount;
  private final List<Observation> observations;
  private final String billingCenter;
  private final String healthComplex;

  public CreateMedicalCenterParams(
      String holderId,
      String groupCode,
      ParametricValueId treatment,
      String name,
      LocalDate registrationDate,
      Address address,
      List<String> phones,
      HealthAuthorization healthAuthorization,
      CenterClass centerClass,
      Contact portalContact,
      Contact communicationContact,
      Contact medicalDirectionContact,
      List<Contact> billingContacts,
      List<Contact> authorizationContacts,
      Boolean publishableInGuides,
      Boolean onlineAppointment,
      ValidityPeriod validityPeriod,
      BillingIndicators billingIndicators,
      BankAccount bankAccount,
      List<Observation> observations,
      String billingCenter,
      String healthComplex) {
    super();

    ValueValidator.ensureNotNull(holderId, "holderId");
    ValueValidator.ensureNotNull(name, "name");
    ValueValidator.ensureNotNull(registrationDate, "registrationDate");
    ValueValidator.ensureNotNull(address, "address");

    this.holderId = holderId;
    this.groupCode = groupCode;
    this.treatment = treatment;
    this.name = name;
    this.registrationDate = registrationDate;
    this.address = address;
    this.phones = phones;
    this.healthAuthorization = healthAuthorization;
    this.centerClass = centerClass;
    this.portalContact = portalContact;
    this.communicationContact = communicationContact;
    this.medicalDirectionContact = medicalDirectionContact;
    this.billingContacts = billingContacts;
    this.authorizationContacts = authorizationContacts;
    this.publishableInGuides = publishableInGuides;
    this.onlineAppointment = onlineAppointment;
    this.validityPeriod = validityPeriod;
    this.billingIndicators = billingIndicators;
    this.bankAccount = bankAccount;
    this.observations = observations;
    this.billingCenter = billingCenter;
    this.healthComplex = healthComplex;
  }
}

