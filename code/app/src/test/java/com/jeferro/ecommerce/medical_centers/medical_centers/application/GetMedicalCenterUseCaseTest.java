package com.jeferro.ecommerce.medical_centers.medical_centers.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jeferro.ecommerce.medical_centers.medical_centers.application.params.GetMedicalCenterParams;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.exceptions.MedicalCenterNotFoundException;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenterId;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.repositories.MedicalCenterInMemoryRepository;
import com.jeferro.ecommerce.shared.domain.models.auth.AuthMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetMedicalCenterUseCaseTest {

  private GetMedicalCenterUseCase getMedicalCenterUseCase;
  private MedicalCenterInMemoryRepository medicalCenterInMemoryRepository;

  @BeforeEach
  void beforeEach() {
    medicalCenterInMemoryRepository = new MedicalCenterInMemoryRepository();
    getMedicalCenterUseCase = new GetMedicalCenterUseCase(medicalCenterInMemoryRepository);
  }

  @Test
  void should_returnMedicalCenter_when_exists() {
    var medicalCenterId = MedicalCenterId.createOf("medical-center-001");
    var medicalCenter = createTestMedicalCenter(medicalCenterId);
    medicalCenterInMemoryRepository.save(medicalCenter);

    var params = new GetMedicalCenterParams(medicalCenterId);

    var result = getMedicalCenterUseCase.execute(AuthMother.john(), params);

    assertEquals(medicalCenter, result);
  }

  @Test
  void should_failedAsMedicalCenterNotFound_when_notExist() {
    var medicalCenterId = MedicalCenterId.createOf("non-existent-id");
    var params = new GetMedicalCenterParams(medicalCenterId);

    assertThrows(
        MedicalCenterNotFoundException.class,
        () -> getMedicalCenterUseCase.execute(AuthMother.john(), params));
  }

  private MedicalCenter createTestMedicalCenter(MedicalCenterId id) {
    return MedicalCenter.create(
        id,
        com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("28"),
        1L,
        "holder-001",
        null,
        null,
        "Centro Médico Test",
        java.time.LocalDate.now(),
        new com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.Address(
            com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("1"),
            "Calle Test",
            "1",
            null,
            null,
            "28001",
            "Madrid",
            com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("28"),
            com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId.createOf("1")
        ),
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
  }
}


