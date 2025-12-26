package com.jeferro.ecommerce.medical_centers.medical_centers.infrastructure.mongo;

import com.jeferro.ecommerce.medical_centers.medical_centers.domain.services.HolderFinder;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class MongoHolderFinder implements HolderFinder {

  @Override
  public boolean exists(String holderId) {
    // TODO: Implementar consulta a MongoDB o servicio externo para validar existencia del titular
    // Por ahora retornamos true para que compile
    // En producción esto debe consultar la base de datos o servicio externo
    return true;
  }

  @Override
  public LocalDate getRegistrationDate(String holderId) {
    // TODO: Implementar consulta a MongoDB o servicio externo para obtener fecha de alta del titular
    // Por ahora retornamos null para que compile
    // En producción esto debe consultar la base de datos o servicio externo
    return null;
  }
}

