package com.jeferro.products.products.infrastructure.kafka;

import com.jeferro.products.products.domain.events.ProductVersionEvent;
import com.jeferro.products.products.infrastructure.kafka.mappers.ProductKafkaMapper;
import com.jeferro.products.shared.infrastructure.config.products.ProductsComponentProperties;
import com.jeferro.shared.ddd.domain.events.EventBusProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventKafkaProducer implements EventBusProducer<ProductVersionEvent> {

  private final ProductKafkaMapper productKafkaMapper = ProductKafkaMapper.INSTANCE;

  private final ProductsComponentProperties productsComponentProperties;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Override
  public void send(ProductVersionEvent event) {
    String key = event.getEntityId().toString();
    var data = productKafkaMapper.toDTO(event);

    kafkaTemplate.send(productsComponentProperties.getTopic(), key, data);
  }
}
