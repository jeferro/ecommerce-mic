package com.jeferro.shared.mappers;

import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

public abstract class AggregateSecondaryMapper<
    Aggregate extends AggregateRoot<Identifier>, Identifier extends StringIdentifier, DTO> {

  @Named("idToDomain")
  public abstract Identifier toDomain(String value);

  @Named("idToDTO")
  public String toDTO(Identifier id) {
    return id.toString();
  }

  @Mapping(target = "id", qualifiedByName = "idToDomain")
  public abstract Aggregate toDomain(DTO dto);

  @Mapping(target = "id", qualifiedByName = "idToDTO")
  public abstract DTO toDTO(Aggregate entity);

  public List<Aggregate> toDomain(List<DTO> elements) {
    return elements.stream().map(this::toDomain).toList();
  }

  protected LocalizedField toDomain(Map<String, String> values) {
    return new LocalizedField(values);
  }

  protected Map<String, String> toDTO(LocalizedField entity) {
    return entity.getValues();
  }
}
