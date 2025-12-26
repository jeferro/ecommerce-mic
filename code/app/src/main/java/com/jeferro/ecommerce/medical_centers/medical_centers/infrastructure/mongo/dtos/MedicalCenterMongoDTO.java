package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.dtos;

import com.jeferro.shared.auth.infrastructure.mongo.dtos.AuditedMongoDTO;
import com.jeferro.shared.auth.infrastructure.mongo.dtos.MetadataMongoDTO;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Getter
@Document("medical_centers")
public class MedicalCenterMongoDTO extends AuditedMongoDTO {

  private final String id;
  private final String code;
  private final String holderId;
  private final String groupCode;
  private final String treatment;
  private final String name;
  private final LocalDate registrationDate;
  private final AddressMongoDTO address;
  private final List<String> phones;
  private final HealthAuthorizationMongoDTO healthAuthorization;
  private final CenterClassMongoDTO centerClass;
  private final LocalDate deactivationDate;
  private final String deactivationReason;
  private final String status;
  private final ContactMongoDTO portalContact;
  private final ContactMongoDTO communicationContact;
  private final ContactMongoDTO medicalDirectionContact;
  private final List<ContactMongoDTO> billingContacts;
  private final List<ContactMongoDTO> authorizationContacts;
  private final boolean publishableInGuides;
  private final boolean onlineAppointment;
  private final ValidityPeriodMongoDTO validityPeriod;
  private final BillingIndicatorsMongoDTO billingIndicators;
  private final BankAccountMongoDTO bankAccount;
  private final List<ObservationMongoDTO> observations;
  private final List<HolderHistoryEntryMongoDTO> holderHistory;
  private final String billingCenter;
  private final String healthComplex;

  public MedicalCenterMongoDTO(
      String id,
      String code,
      String holderId,
      String groupCode,
      String treatment,
      String name,
      LocalDate registrationDate,
      AddressMongoDTO address,
      List<String> phones,
      HealthAuthorizationMongoDTO healthAuthorization,
      CenterClassMongoDTO centerClass,
      LocalDate deactivationDate,
      String deactivationReason,
      String status,
      ContactMongoDTO portalContact,
      ContactMongoDTO communicationContact,
      ContactMongoDTO medicalDirectionContact,
      List<ContactMongoDTO> billingContacts,
      List<ContactMongoDTO> authorizationContacts,
      boolean publishableInGuides,
      boolean onlineAppointment,
      ValidityPeriodMongoDTO validityPeriod,
      BillingIndicatorsMongoDTO billingIndicators,
      BankAccountMongoDTO bankAccount,
      List<ObservationMongoDTO> observations,
      List<HolderHistoryEntryMongoDTO> holderHistory,
      String billingCenter,
      String healthComplex,
      Long version,
      MetadataMongoDTO metadata) {
    super(version, metadata);
    this.id = id;
    this.code = code;
    this.holderId = holderId;
    this.groupCode = groupCode;
    this.treatment = treatment;
    this.name = name;
    this.registrationDate = registrationDate;
    this.address = address;
    this.phones = phones;
    this.healthAuthorization = healthAuthorization;
    this.centerClass = centerClass;
    this.deactivationDate = deactivationDate;
    this.deactivationReason = deactivationReason;
    this.status = status;
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
    this.holderHistory = holderHistory;
    this.billingCenter = billingCenter;
    this.healthComplex = healthComplex;
  }

  @Getter
  public static class AddressMongoDTO {
    private final String streetType;
    private final String streetName;
    private final String number;
    private final String floor;
    private final String door;
    private final String postalCode;
    private final String city;
    private final String province;
    private final String country;

    public AddressMongoDTO(
        String streetType,
        String streetName,
        String number,
        String floor,
        String door,
        String postalCode,
        String city,
        String province,
        String country) {
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

  @Getter
  public static class ContactMongoDTO {
    private final String name;
    private final String nif;
    private final String phone;
    private final String email;
    private final String ppsPartialAdminUserCode;

    public ContactMongoDTO(
        String name,
        String nif,
        String phone,
        String email,
        String ppsPartialAdminUserCode) {
      this.name = name;
      this.nif = nif;
      this.phone = phone;
      this.email = email;
      this.ppsPartialAdminUserCode = ppsPartialAdminUserCode;
    }
  }

  @Getter
  public static class HealthAuthorizationMongoDTO {
    private final String autonomousCommunity;
    private final String registrationNumber;

    public HealthAuthorizationMongoDTO(String autonomousCommunity, String registrationNumber) {
      this.autonomousCommunity = autonomousCommunity;
      this.registrationNumber = registrationNumber;
    }
  }

  @Getter
  public static class CenterClassMongoDTO {
    private final String type;
    private final List<String> hospitalClasses;
    private final List<String> extrahospitalClasses;

    public CenterClassMongoDTO(
        String type,
        List<String> hospitalClasses,
        List<String> extrahospitalClasses) {
      this.type = type;
      this.hospitalClasses = hospitalClasses;
      this.extrahospitalClasses = extrahospitalClasses;
    }
  }

  @Getter
  public static class ValidityPeriodMongoDTO {
    private final LocalDate startDate;
    private final LocalDate endDate;

    public ValidityPeriodMongoDTO(LocalDate startDate, LocalDate endDate) {
      this.startDate = startDate;
      this.endDate = endDate;
    }
  }

  @Getter
  public static class BillingIndicatorsMongoDTO {
    private final String changeReasons;
    private final Boolean ownershipChange;
    private final Boolean pendingAssistancePayment;
    private final Boolean notApplicablePreviousIndicator;
    private final ReconciliationMongoDTO reconciliation;
    private final List<BillingIndicatorChangeMongoDTO> changeHistory;

    public BillingIndicatorsMongoDTO(
        String changeReasons,
        Boolean ownershipChange,
        Boolean pendingAssistancePayment,
        Boolean notApplicablePreviousIndicator,
        ReconciliationMongoDTO reconciliation,
        List<BillingIndicatorChangeMongoDTO> changeHistory) {
      this.changeReasons = changeReasons;
      this.ownershipChange = ownershipChange;
      this.pendingAssistancePayment = pendingAssistancePayment;
      this.notApplicablePreviousIndicator = notApplicablePreviousIndicator;
      this.reconciliation = reconciliation;
      this.changeHistory = changeHistory;
    }
  }

  @Getter
  public static class ReconciliationMongoDTO {
    private final boolean reconciles;
    private final String reconciliationMethod;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String changeReason;
    private final Boolean ownershipChange;
    private final Boolean pendingAssistancePayment;
    private final Boolean notApplicablePreviousIndicator;

    public ReconciliationMongoDTO(
        boolean reconciles,
        String reconciliationMethod,
        LocalDate startDate,
        LocalDate endDate,
        String changeReason,
        Boolean ownershipChange,
        Boolean pendingAssistancePayment,
        Boolean notApplicablePreviousIndicator) {
      this.reconciles = reconciles;
      this.reconciliationMethod = reconciliationMethod;
      this.startDate = startDate;
      this.endDate = endDate;
      this.changeReason = changeReason;
      this.ownershipChange = ownershipChange;
      this.pendingAssistancePayment = pendingAssistancePayment;
      this.notApplicablePreviousIndicator = notApplicablePreviousIndicator;
    }
  }

  @Getter
  public static class BillingIndicatorChangeMongoDTO {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String changeReason;
    private final Boolean ownershipChange;
    private final Boolean pendingAssistancePayment;
    private final Boolean notApplicablePreviousIndicator;

    public BillingIndicatorChangeMongoDTO(
        LocalDate startDate,
        LocalDate endDate,
        String changeReason,
        Boolean ownershipChange,
        Boolean pendingAssistancePayment,
        Boolean notApplicablePreviousIndicator) {
      this.startDate = startDate;
      this.endDate = endDate;
      this.changeReason = changeReason;
      this.ownershipChange = ownershipChange;
      this.pendingAssistancePayment = pendingAssistancePayment;
      this.notApplicablePreviousIndicator = notApplicablePreviousIndicator;
    }
  }

  @Getter
  public static class BankAccountMongoDTO {
    private final String iban;
    private final String swift;
    private final String accountHolder;

    public BankAccountMongoDTO(String iban, String swift, String accountHolder) {
      this.iban = iban;
      this.swift = swift;
      this.accountHolder = accountHolder;
    }
  }

  @Getter
  public static class ObservationMongoDTO {
    private final boolean publishable;
    private final String text;

    public ObservationMongoDTO(boolean publishable, String text) {
      this.publishable = publishable;
      this.text = text;
    }
  }

  @Getter
  public static class HolderHistoryEntryMongoDTO {
    private final String holderCode;
    private final String holderName;
    private final LocalDate registrationDate;
    private final LocalDate deactivationDate;

    public HolderHistoryEntryMongoDTO(
        String holderCode,
        String holderName,
        LocalDate registrationDate,
        LocalDate deactivationDate) {
      this.holderCode = holderCode;
      this.holderName = holderName;
      this.registrationDate = registrationDate;
      this.deactivationDate = deactivationDate;
    }
  }
}


