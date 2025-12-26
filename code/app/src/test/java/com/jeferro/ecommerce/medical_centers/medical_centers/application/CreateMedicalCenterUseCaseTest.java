package com.jeferro.ecommerce.medical_centers.medical_centers.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jeferro.ecommerce.medical_centers.medical_centers.application.params.CreateMedicalCenterParams;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions.MedicalCenterAlreadyExistsException;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.repositories.MedicalCenterInMemoryRepository;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.services.HolderFinder;
import com.jeferro.ecommerce.shared.domain.events.EventInMemoryBus;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.services.SequenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CreateMedicalCenterUseCaseTest {

  private MedicalCenterInMemoryRepository medicalCenterInMemoryRepository;
  private EventInMemoryBus eventInMemoryBus;
  @Mock
  private SequenceGenerator sequenceGenerator;
  @Mock
  private HolderFinder holderFinder;
  private CreateMedicalCenterUseCase createMedicalCenterUseCase;

  @BeforeEach
  void beforeEach() {
    MockitoAnnotations.openMocks(this);
    medicalCenterInMemoryRepository = new MedicalCenterInMemoryRepository();
    eventInMemoryBus = new EventInMemoryBus();

    when(sequenceGenerator.generate(any(Class.class))).thenReturn("000001");
    when(holderFinder.exists(anyString())).thenReturn(true);
    when(holderFinder.getRegistrationDate(anyString())).thenReturn(null);

    createMedicalCenterUseCase = new CreateMedicalCenterUseCase(
        medicalCenterInMemoryRepository,
        eventInMemoryBus,
        sequenceGenerator,
        holderFinder);
  }

  @Test
  void should_createMedicalCenter_when_validData() {
    var provinceCode = com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("28");
    var address = new com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.Address(
        com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("1"),
        "Calle Test",
        "1",
        null,
        null,
        "28001",
        "Madrid",
        provinceCode,
        com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("1")
    );

    var params = new CreateMedicalCenterParams(
        "holder-001",
        null,
        null,
        "Centro Medico Test",
        java.time.LocalDate.now(),
        address,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    var result = createMedicalCenterUseCase.execute(AuthMother.john(), params);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertNotNull(result.getCode());
    assertEquals("holder-001", result.getHolderId());
    assertEquals("Centro Medico Test", result.getName());
    assertTrue(medicalCenterInMemoryRepository.contains(result));
  }

  @Test
  void should_failedAsMedicalCenterAlreadyExists_when_sameNameAddressAndHolder() {
    var provinceCode = com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("28");
    var address = new com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.Address(
        com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("1"),
        "Calle Test",
        "1",
        null,
        null,
        "28001",
        "Madrid",
        provinceCode,
        com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("1")
    );

    var existingCenter = createTestMedicalCenter(
        com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId.createOf("2800001"),
        provinceCode,
        address);
    medicalCenterInMemoryRepository.save(existingCenter);

    var params = new CreateMedicalCenterParams(
        "holder-001",
        null,
        null,
        "Centro Medico Test",
        java.time.LocalDate.now(),
        address,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    assertThrows(
        MedicalCenterAlreadyExistsException.class,
        () -> createMedicalCenterUseCase.execute(AuthMother.john(), params));
  }

  private MedicalCenter createTestMedicalCenter(
      com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId id,
      com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId provinceCode,
      com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.Address address) {
    return MedicalCenter.create(
        id,                                    // MedicalCenterId id
        provinceCode,                          // ParametricValueId provinceCode
        1L,                                    // long sequentialNumber
        "holder-001",                          // String holderId
        null,                                  // String groupCode
        null,                                  // ParametricValueId treatment
        "Centro Medico Test",                 // String name
        java.time.LocalDate.now(),            // LocalDate registrationDate
        address,                               // Address address
        null,                                  // List<String> phones
        null,                                  // HealthAuthorization healthAuthorization
        null,                                  // CenterClass centerClass
        null,                                  // Contact portalContact
        null,                                  // Contact communicationContact
        null,                                  // Contact medicalDirectionContact
        null,                                  // List<Contact> billingContacts
        null,                                  // List<Contact> authorizationContacts
        null,                                  // Boolean publishableInGuides
        null,                                  // Boolean onlineAppointment
        null,                                  // LocalDate validityPeriodEndDate
        null,                                  // BillingIndicators billingIndicators
        null,                                  // BankAccount bankAccount
        null,                                  // List<Observation> observations
        null,                                  // String billingCenter
        null                                   // String healthComplex
    );
  }
}

