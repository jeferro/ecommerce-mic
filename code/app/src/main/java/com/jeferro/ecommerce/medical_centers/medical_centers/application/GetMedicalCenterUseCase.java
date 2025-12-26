package com.jeferro.ecommerce.medical_centers.medical_centers.application;

import static com.jeferro.ecommerce.shared.domain.models.Roles.USER;

import com.jeferro.ecommerce.medical_centers.medical_centers.application.params.GetMedicalCenterParams;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.models.MedicalCenter;
import com.jeferro.ecommerce.medical_centers.medical_centers.domain.repositories.MedicalCenterRepository;
import com.jeferro.shared.ddd.application.UseCase;
import com.jeferro.shared.ddd.domain.models.auth.Auth;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMedicalCenterUseCase extends UseCase<GetMedicalCenterParams, MedicalCenter> {

  private final MedicalCenterRepository medicalCenterRepository;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public MedicalCenter execute(Auth auth, GetMedicalCenterParams params) {
    var medicalCenterId = params.getMedicalCenterId();

    return medicalCenterRepository.findByIdOrError(medicalCenterId);
  }
}


