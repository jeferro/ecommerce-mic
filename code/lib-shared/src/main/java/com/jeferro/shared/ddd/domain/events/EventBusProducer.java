package com.jeferro.shared.ddd.domain.events;

public interface EventBusProducer<E extends Event> {

  void send(E event) throws Exception;
}
