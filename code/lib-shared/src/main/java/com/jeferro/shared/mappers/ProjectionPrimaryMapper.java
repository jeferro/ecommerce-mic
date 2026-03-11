package com.jeferro.shared.mappers;

import com.jeferro.shared.auth.infrastructure.ContextManager;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import com.jeferro.shared.ddd.domain.models.aggregates.StringIdentifier;
import com.jeferro.shared.ddd.domain.models.projection.Projection;
import com.jeferro.shared.locale.domain.models.LocalizedField;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

public abstract class ProjectionPrimaryMapper<
    Project extends Projection<Identifier>, Identifier extends StringIdentifier, DTO> {

  public abstract DTO toDTO(Project entity);

  public abstract List<DTO> toDTO(PaginatedList<Project> entities);

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
