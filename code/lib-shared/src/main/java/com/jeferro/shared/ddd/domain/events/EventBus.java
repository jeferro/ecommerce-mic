package com.jeferro.shared.ddd.domain.events;

import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Identifier;
import com.jeferro.shared.ddd.domain.models.aggregates.PaginatedList;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EventBus {

  private final Map<Class<Event>, List<EventBusProducer<?>>> producers = new HashMap<>();

  public <I extends Identifier, E extends AggregateRoot<I>> void sendAll(
      PaginatedList<E> aggregateRoots) {
    aggregateRoots.forEach(this::sendAll);
  }

  public <I extends Identifier, E extends AggregateRoot<I>> void sendAll(E aggregateRoot) {
    aggregateRoot.pullEvents().forEach(this::send);
  }

  protected void send(Event event) {
    Class<?> eventClass = event.getClass();

    sendEventByEventClass(event, eventClass);
  }

  private void sendEventByEventClass(Event event, Class<?> eventClass) {
    if (producers.containsKey(eventClass)) {
      producers.get(eventClass).forEach(producer -> sendEvent(producer, event));
    }

    Class<?> superEventClass = eventClass.getSuperclass();

    if (!superEventClass.equals(Object.class)) {
      sendEventByEventClass(event, superEventClass);
    }
  }

  private <E extends Event> void sendEvent(EventBusProducer<?> producer, E event) {
    try {
      ((EventBusProducer<E>) producer).send(event);
    } catch (Exception cause) {
      throw new RuntimeException(cause);
    }
  }

  protected void registryProducer(EventBusProducer<?> eventBusProducer) {
    Type type = eventBusProducer.getClass().getGenericInterfaces()[0];

    if (!(type instanceof ParameterizedType parameterizedType)) {
      throw new IllegalArgumentException("Event superclass is not a parameterized type");
    }

    Class<Event> eventClass = (Class<Event>) parameterizedType.getActualTypeArguments()[0];

    if (producers.containsKey(eventClass)) {
      producers.get(eventClass).add(eventBusProducer);
      return;
    }

    List<EventBusProducer<?>> producersByEventClass = new ArrayList<>();
    producersByEventClass.add(eventBusProducer);

    producers.put(eventClass, producersByEventClass);
  }
}
