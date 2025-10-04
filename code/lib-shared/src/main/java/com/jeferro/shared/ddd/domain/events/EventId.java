package com.jeferro.shared.ddd.domain.events;

import com.jeferro.shared.ddd.domain.models.aggregates.UUID;

public class EventId extends UUID {

  public EventId(String value) {
    super(value);
  }

  private EventId() {
    super();
  }

  public static EventId create() {
    return new EventId();
  }
}
