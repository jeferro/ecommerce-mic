package com.jeferro.ecommerce.shared.domain.events;

import com.jeferro.shared.ddd.domain.events.Event;
import com.jeferro.shared.ddd.domain.events.EventBus;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EventFakeBus extends EventBus {

  private final List<Event> events = new ArrayList<>();

  @Override
  protected void send(Event event) {
    events.add(event);
  }

  public int size() {
    return events.size();
  }

  public <T> Stream<T> filterOfClass(Class<T> clazz) {
    return events.stream().filter(clazz::isInstance).map(clazz::cast);
  }

  public boolean isEmpty() {
    return events.isEmpty();
  }

  public void forEach(Consumer<Event> action) {
    events.forEach(action);
  }
}
