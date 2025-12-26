package com.jeferro.ecommerce.medical_centers.medical_centers.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.medical_centers.medical_centers.application.params.CreateMedicalCenterParams;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions.HolderNotFoundException;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions.InvalidAddressContactDataException;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions.MedicalCenterAlreadyExistsException;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions.RegistrationDateBeforeHolderException;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.Contact;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.criteria.MedicalCenterCriteria;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.repositories.MedicalCenterRepository;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.services.HolderFinder;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.services.SequenceGenerator;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMedicalCenterUseCase extends UseCase<CreateMedicalCenterParams, MedicalCenter> {

  private static final String UNKNOWN_ADDRESS_TYPE = "25";

  private final MedicalCenterRepository medicalCenterRepository;
  private final EventBus eventBus;
  private final SequenceGenerator sequenceGenerator;
  private final HolderFinder holderFinder;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public MedicalCenter execute(Auth auth, CreateMedicalCenterParams params) {
    ensureHolderExists(params);
    ensureRegistrationDateNotBeforeHolder(params);
    ensureMedicalCenterDoesNotExist(params);
    ensureAddressContactDataValid(params);

    var medicalCenter = createMedicalCenter(params);

    medicalCenterRepository.save(medicalCenter);

    eventBus.sendAll(medicalCenter);

    return medicalCenter;
  }

  private void ensureHolderExists(CreateMedicalCenterParams params) {
    var holderId = params.getHolderId();
    if (!holderFinder.exists(holderId)) {
      throw HolderNotFoundException.createOf(holderId);
    }
  }

  private void ensureRegistrationDateNotBeforeHolder(CreateMedicalCenterParams params) {
    var holderId = params.getHolderId();
    var holderRegistrationDate = holderFinder.getRegistrationDate(holderId);
    var registrationDate = params.getRegistrationDate();

    if (holderRegistrationDate != null && registrationDate.isBefore(holderRegistrationDate)) {
      throw RegistrationDateBeforeHolderException.createOf(registrationDate, holderRegistrationDate);
    }
  }

  private void ensureMedicalCenterDoesNotExist(CreateMedicalCenterParams params) {
    var criteria = MedicalCenterCriteria.byHolderId(params.getHolderId());
    var existingCenters = medicalCenterRepository.findAll(criteria);

    var duplicate = existingCenters.stream()
        .filter(center -> center.getName().equals(params.getName()))
        .filter(center -> addressesMatch(center.getAddress(), params.getAddress()))
        .findFirst();

    if (duplicate.isPresent()) {
      throw MedicalCenterAlreadyExistsException.createOf(duplicate.get().getId());
    }
  }

  private void ensureAddressContactDataValid(CreateMedicalCenterParams params) {
    var address = params.getAddress();
    var isUnknownAddress = UNKNOWN_ADDRESS_TYPE.equals(address.getStreetType().toString());

    if (!isUnknownAddress) {
      return;
    }

    ensureOnlyAllowedContactFieldsForUnknownAddress(
        params.getPortalContact(), "portalContact");
    ensureOnlyAllowedContactFieldsForUnknownAddress(
        params.getCommunicationContact(), "communicationContact");
    ensureOnlyAllowedContactFieldsForUnknownAddress(
        params.getMedicalDirectionContact(), "medicalDirectionContact");

    if (params.getBillingContacts() != null) {
      for (var i = 0; i < params.getBillingContacts().size(); i++) {
        ensureOnlyAllowedContactFieldsForUnknownAddress(
            params.getBillingContacts().get(i), "billingContacts[" + i + "]");
      }
    }

    if (params.getAuthorizationContacts() != null) {
      for (var i = 0; i < params.getAuthorizationContacts().size(); i++) {
        ensureOnlyAllowedContactFieldsForUnknownAddress(
            params.getAuthorizationContacts().get(i), "authorizationContacts[" + i + "]");
      }
    }

    if (address.getStreetName() != null && !address.getStreetName().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          "streetName must be empty when streetType is 25 (Unknown address)");
    }

    if (address.getNumber() != null && !address.getNumber().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          "number must be empty when streetType is 25 (Unknown address)");
    }

    if (address.getFloor() != null && !address.getFloor().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          "floor must be empty when streetType is 25 (Unknown address)");
    }

    if (address.getDoor() != null && !address.getDoor().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          "door must be empty when streetType is 25 (Unknown address)");
    }

    if (address.getCity() != null && !address.getCity().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          "city must be empty when streetType is 25 (Unknown address)");
    }
  }

  private void ensureOnlyAllowedContactFieldsForUnknownAddress(
      Contact contact, String contactName) {
    if (contact == null) {
      return;
    }

    if (contact.getName() != null && !contact.getName().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          contactName
              + ".name must be empty when streetType is 25 (Unknown address). Only phone, postalCode, province and city are allowed");
    }

    if (contact.getNif() != null && !contact.getNif().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          contactName
              + ".nif must be empty when streetType is 25 (Unknown address). Only phone, postalCode, province and city are allowed");
    }

    if (contact.getEmail() != null && !contact.getEmail().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          contactName
              + ".email must be empty when streetType is 25 (Unknown address). Only phone, postalCode, province and city are allowed");
    }

    if (contact.getPpsPartialAdminUserCode() != null
        && !contact.getPpsPartialAdminUserCode().isBlank()) {
      throw InvalidAddressContactDataException.createOf(
          contactName
              + ".ppsPartialAdminUserCode must be empty when streetType is 25 (Unknown address). Only phone, postalCode, province and city are allowed");
    }
  }

  private boolean addressesMatch(
      com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.Address address1,
      com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.Address address2) {
    if (address1 == null || address2 == null) {
      return address1 == address2;
    }

    return address1.getStreetType().equals(address2.getStreetType())
        && equalsOrBothNull(address1.getStreetName(), address2.getStreetName())
        && equalsOrBothNull(address1.getNumber(), address2.getNumber())
        && equalsOrBothNull(address1.getFloor(), address2.getFloor())
        && equalsOrBothNull(address1.getDoor(), address2.getDoor())
        && equalsOrBothNull(address1.getPostalCode(), address2.getPostalCode())
        && equalsOrBothNull(address1.getCity(), address2.getCity())
        && equalsOrBothNull(address1.getProvince(), address2.getProvince())
        && equalsOrBothNull(address1.getCountry(), address2.getCountry());
  }

  private boolean equalsOrBothNull(Object obj1, Object obj2) {
    if (obj1 == null && obj2 == null) {
      return true;
    }
    if (obj1 == null || obj2 == null) {
      return false;
    }
    return obj1.equals(obj2);
  }

  private MedicalCenter createMedicalCenter(CreateMedicalCenterParams params) {
    var provinceCode = params.getAddress().getProvince();
    if (provinceCode == null) {
      throw new IllegalArgumentException("Province code is required in address");
    }

    var sequentialNumberStr = sequenceGenerator.generate(
        com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo.dtos.MedicalCenterMongoDTO.class);
    var sequentialNumber = Long.parseLong(sequentialNumberStr);

    var code = com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterCode
        .createOf(provinceCode, sequentialNumber);
    var medicalCenterId = MedicalCenterId.createOf(code.toString());

    var validityPeriodEndDate = params.getValidityPeriod() != null
        ? params.getValidityPeriod().getEndDate()
        : null;

    return MedicalCenter.create(
        medicalCenterId,
        provinceCode,
        sequentialNumber,
        params.getHolderId(),
        params.getGroupCode(),
        params.getTreatment(),
        params.getName(),
        params.getRegistrationDate(),
        params.getAddress(),
        params.getPhones(),
        params.getHealthAuthorization(),
        params.getCenterClass(),
        params.getPortalContact(),
        params.getCommunicationContact(),
        params.getMedicalDirectionContact(),
        params.getBillingContacts(),
        params.getAuthorizationContacts(),
        params.getPublishableInGuides(),
        params.getOnlineAppointment(),
        validityPeriodEndDate,
        params.getBillingIndicators(),
        params.getBankAccount(),
        params.getObservations(),
        params.getBillingCenter(),
        params.getHealthComplex());
  }
}

