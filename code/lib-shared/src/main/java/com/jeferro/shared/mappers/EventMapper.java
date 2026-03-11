package com.jeferro.shared.mappers;

import com.jeferro.shared.ddd.domain.events.Event;
import com.jeferro.shared.ddd.domain.models.aggregates.Identifier;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.Map;

public abstract class EventMapper<T extends Event> {

  public String toDTO(Identifier id) {
    return id.toString();
  }

  protected LocalizedField toDomain(Map<String, String> values) {
    return new LocalizedField(values);
  }

  protected Map<String, String> toDTO(LocalizedField entity) {
    return entity.getValues();
  }
}
