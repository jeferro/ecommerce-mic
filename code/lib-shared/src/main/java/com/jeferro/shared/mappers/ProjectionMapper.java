package com.jeferro.shared.mappers;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.models.projection.Projection;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.Map;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

public abstract class ProjectionMapper<
    Project extends Projection<Identifier>, Identifier extends StringIdentifier, DTO> {

  @Named("idToDomain")
  public abstract Identifier toDomain(String value);

  @Mapping(target = "id", qualifiedByName = "idToDomain")
  public abstract Project toDomain(DTO dto);

  public String toDTO(Identifier id) {
    return id.getValue();
  }

  protected LocalizedField toDomain(Map<String, String> values) {
    return new LocalizedField(values);
  }
}
