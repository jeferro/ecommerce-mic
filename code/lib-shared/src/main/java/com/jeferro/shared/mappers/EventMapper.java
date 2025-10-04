package com.jeferro.shared.mappers;

import com.jeferro.shared.ddd.domain.events.Event;
import com.jeferro.shared.locale.domain.models.LocalizedField;
import java.util.Map;

public abstract class EventMapper<T extends Event> {

  protected LocalizedField toDomain(Map<String, String> values) {
    return new LocalizedField(values);
  }

  protected Map<String, String> toDTO(LocalizedField entity) {
    return entity.getValues();
  }
}
