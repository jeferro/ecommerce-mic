package com.jeferro.ecommerce.medical_centers.medical_centers.domain.models;

import static com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.status.MedicalCenterStatus.ACTIVE;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.status.MedicalCenterStatus;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MedicalCenter extends AggregateRoot<MedicalCenterId> {

  private static final int MAX_NAME_LENGTH = 100;
  private static final int MAX_PHONES = 3;
  private static final int MAX_BILLING_CONTACTS = 2;
  private static final int MAX_AUTHORIZATION_CONTACTS = 2;

  private final MedicalCenterCode code;
  private final String holderId;
  private final String groupCode;
  private final ParametricValueId treatment;
  private final String name;
  private final LocalDate registrationDate;
  private final Address address;
  private final List<String> phones;
  private final HealthAuthorization healthAuthorization;
  private final CenterClass centerClass;
  private final LocalDate deactivationDate;
  private final ParametricValueId deactivationReason;
  private final MedicalCenterStatus status;
  private final Contact portalContact;
  private final Contact communicationContact;
  private final Contact medicalDirectionContact;
  private final List<Contact> billingContacts;
  private final List<Contact> authorizationContacts;
  private final boolean publishableInGuides;
  private final boolean onlineAppointment;
  private final ValidityPeriod validityPeriod;
  private final BillingIndicators billingIndicators;
  private final BankAccount bankAccount;
  private final List<Observation> observations;
  private final List<HolderHistoryEntry> holderHistory;
  private final String billingCenter;
  private final String healthComplex;

  public MedicalCenter(MedicalCenterId id,
                      MedicalCenterCode code,
                      String holderId,
                      String groupCode,
                      ParametricValueId treatment,
                      String name,
                      LocalDate registrationDate,
                      Address address,
                      List<String> phones,
                      HealthAuthorization healthAuthorization,
                      CenterClass centerClass,
                      LocalDate deactivationDate,
                      ParametricValueId deactivationReason,
                      MedicalCenterStatus status,
                      Contact portalContact,
                      Contact communicationContact,
                      Contact medicalDirectionContact,
                      List<Contact> billingContacts,
                      List<Contact> authorizationContacts,
                      boolean publishableInGuides,
                      boolean onlineAppointment,
                      ValidityPeriod validityPeriod,
                      BillingIndicators billingIndicators,
                      BankAccount bankAccount,
                      List<Observation> observations,
                      List<HolderHistoryEntry> holderHistory,
                      String billingCenter,
                      String healthComplex,
                      long version,
                      Metadata metadata) {
    super(id, version, metadata);

    this.code = code;
    this.holderId = holderId;
    this.groupCode = groupCode;
    this.treatment = treatment;
    this.name = name;
    this.registrationDate = registrationDate;
    this.address = address;
    this.phones = phones != null ? new ArrayList<>(phones) : new ArrayList<>();
    this.healthAuthorization = healthAuthorization;
    this.centerClass = centerClass;
    this.deactivationDate = deactivationDate;
    this.deactivationReason = deactivationReason;
    this.status = status;
    this.portalContact = portalContact;
    this.communicationContact = communicationContact;
    this.medicalDirectionContact = medicalDirectionContact;
    this.billingContacts = billingContacts != null ? new ArrayList<>(billingContacts) : new ArrayList<>();
    this.authorizationContacts = authorizationContacts != null ? new ArrayList<>(authorizationContacts) : new ArrayList<>();
    this.publishableInGuides = publishableInGuides;
    this.onlineAppointment = onlineAppointment;
    this.validityPeriod = validityPeriod;
    this.billingIndicators = billingIndicators;
    this.bankAccount = bankAccount;
    this.observations = observations != null ? new ArrayList<>(observations) : new ArrayList<>();
    this.holderHistory = holderHistory != null ? new ArrayList<>(holderHistory) : new ArrayList<>();
    this.billingCenter = billingCenter;
    this.healthComplex = healthComplex;
  }

  public static MedicalCenter create(MedicalCenterId id,
                                     ParametricValueId provinceCode,
                                     long sequentialNumber,
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
                                     LocalDate validityPeriodEndDate,
                                     BillingIndicators billingIndicators,
                                     BankAccount bankAccount,
                                     List<Observation> observations,
                                     String billingCenter,
                                     String healthComplex) {
    ValueValidator.ensureNotNull(id, "id");
    ValueValidator.ensureNotNull(provinceCode, "provinceCode");
    ValueValidator.ensurePositive((int) sequentialNumber, "sequentialNumber");
    ValueValidator.ensureNotNull(holderId, "holderId");
    ValueValidator.ensureNotNull(name, "name");
    ValueValidator.ensureNotNull(registrationDate, "registrationDate");
    ValueValidator.ensureNotNull(address, "address");

    var code = MedicalCenterCode.createOf(provinceCode, sequentialNumber);

    ensureNameFormat(name);
    ensurePhonesSize(phones);
    ensureBillingContactsSize(billingContacts);
    ensureAuthorizationContactsSize(authorizationContacts);

    var publishableInGuidesValue = publishableInGuides != null ? publishableInGuides : true;
    var onlineAppointmentValue = onlineAppointment != null ? onlineAppointment : false;
    var validityPeriod = ValidityPeriod.createWithCurrentDate(validityPeriodEndDate);

    return new MedicalCenter(
        id,
        code,
        holderId,
        groupCode,
        treatment,
        name,
        registrationDate,
        address,
        phones,
        healthAuthorization,
        centerClass,
        null,
        null,
            ACTIVE,
        portalContact,
        communicationContact,
        medicalDirectionContact,
        billingContacts,
        authorizationContacts,
        publishableInGuidesValue,
        onlineAppointmentValue,
        validityPeriod,
        billingIndicators,
        bankAccount,
        observations,
        new ArrayList<>(),
        billingCenter,
        healthComplex,
        0L,
        null
    );
  }

  private static void ensureNameFormat(String name) {
    ValueValidator.ensure(
        () -> name.length() <= MAX_NAME_LENGTH,
        "name exceeds maximum length of " + MAX_NAME_LENGTH);
    ValueValidator.ensure(
        () -> name.matches("^[a-zA-Z0-9\\s]+$"),
        "name must be alphanumeric");
    ValueValidator.ensure(
        () -> !name.contains("\n"),
        "name must be single line");
  }

  private static void ensurePhonesSize(List<String> phones) {
    if (phones != null) {
      ValueValidator.ensure(
          () -> phones.size() <= MAX_PHONES,
          "phones exceeds maximum size of " + MAX_PHONES);
    }
  }

  private static void ensureBillingContactsSize(List<Contact> billingContacts) {
    if (billingContacts != null) {
      ValueValidator.ensure(
          () -> billingContacts.size() <= MAX_BILLING_CONTACTS,
          "billingContacts exceeds maximum size of " + MAX_BILLING_CONTACTS);
    }
  }

  private static void ensureAuthorizationContactsSize(List<Contact> authorizationContacts) {
    if (authorizationContacts != null) {
      ValueValidator.ensure(
          () -> authorizationContacts.size() <= MAX_AUTHORIZATION_CONTACTS,
          "authorizationContacts exceeds maximum size of " + MAX_AUTHORIZATION_CONTACTS);
    }
  }
}

