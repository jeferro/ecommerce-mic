package com.jeferro.shared.mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import com.jeferro.shared.auth.infrastructure.ContextManager;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.locale.domain.models.LocalizedField;

public abstract class AggregateRestMapper<
    Aggregate extends AggregateRoot<Identifier>, Identifier extends StringIdentifier, DTO> {

  public abstract DTO toDTO(Aggregate entity);

  public abstract List<DTO> toDTO(PaginatedList<Aggregate> entities);

  protected LocalizedField toDomain(Map<String, String> values) {
    return new LocalizedField(values);
  }

  protected Map<String, String> toDTO(LocalizedField entity) {
    var locale = ContextManager.getLocale();

    return entity.getValues(locale);
  }

  public OffsetDateTime toDTO(Instant date) {
    if (date == null) {
      return null;
    }

    return date.atOffset(ZoneOffset.UTC);
  }

  public Instant toDomain(OffsetDateTime date) {
    if (date == null) {
      return null;
    }

    return date.toInstant();
  }
}
