package com.jeferro.shared.ddd.infrastructure;

import com.jeferro.shared.ddd.domain.events.EventBus;
import com.jeferro.shared.ddd.domain.events.EventBusProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringEventBus extends EventBus {

  public SpringEventBus(ApplicationContext applicationContext) {
    applicationContext
        .getBeansOfType(EventBusProducer.class)
        .values()
        .forEach(this::registryProducer);
  }
}
