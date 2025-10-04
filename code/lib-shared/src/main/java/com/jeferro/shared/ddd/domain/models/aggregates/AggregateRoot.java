package com.jeferro.shared.ddd.domain.models.aggregates;

import com.jeferro.shared.ddd.domain.events.Event;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

public class AggregateRoot<ID extends Identifier> extends Entity<ID> {

  @ToStringExclude @EqualsExclude private final List<Event> events;

  public AggregateRoot(ID id) {
    super(id);

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
