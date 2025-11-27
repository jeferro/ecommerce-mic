package com.jeferro.shared.mappers;

import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.models.projection.Projection;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.Map;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

public abstract class ProjectionSecondaryMapper<
    Project extends Projection<Identifier>, Identifier extends StringIdentifier, DTO> {

  @Named("idToDomain")
  public abstract Identifier toDomain(String value);

  public String toDTO(Identifier id) {
    return id.toString();
  }

  @Mapping(target = "id", qualifiedByName = "idToDomain")
  public abstract Project toDomain(DTO dto);

  protected LocalizedField toDomain(Map<String, String> values) {
    return new LocalizedField(values);
  }
}
