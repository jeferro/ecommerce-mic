package com.jeferro.shared.ddd.domain.events;

import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Identifier;
import com.jeferro.shared.ddd.domain.models.value_objects.ValueObject;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class Event<A extends AggregateRoot<I>, I extends Identifier> extends ValueObject {

  private final EventId id;

  private final Instant sentAt;

  protected final A entity;

  public Event(A entity) {
    this.id = EventId.create();
    this.sentAt = Instant.now();

    this.entity = entity;
  }

  public I getEntityId() {
    return entity.getId();
  }
}
