package com.jeferro.shared.ddd.domain.models.aggregates;

import java.util.ArrayList;
import java.util.List;

import com.jeferro.shared.ddd.domain.events.Event;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

public class AggregateRoot<ID extends Identifier> extends Entity<ID> {

  @Getter
  private final Metadata metadata;

  @ToStringExclude
  @EqualsExclude
  private final List<Event> events;

  public AggregateRoot(ID id,
      Metadata metadata) {
    super(id);
    this.metadata = metadata;

    events = new ArrayList<>();
  }

  protected void record(Event event) {
    events.add(event);
  }

  public List<Event> pullEvents() {
    List<Event> domainEvents = new ArrayList<>(this.events);

    this.events.clear();

    return domainEvents;
  }
}
