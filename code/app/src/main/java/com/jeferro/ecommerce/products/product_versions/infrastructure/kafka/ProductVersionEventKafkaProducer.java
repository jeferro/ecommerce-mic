package com.jeferro.ecommerce.products.product_versions.infrastructure.kafka;

import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionEvent;
import com.jeferro.ecommerce.products.product_versions.infrastructure.kafka.mappers.ProductVersionKafkaMapper;
import com.jeferro.ecommerce.shared.infrastructure.properties.ApplicationProperties;
import com.jeferro.ecommerce.shared.infrastructure.properties.ProductVersionsProperties;
import com.jeferro.shared.ddd.domain.events.EventBusProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductVersionEventKafkaProducer implements EventBusProducer<ProductVersionEvent> {

  private final ProductVersionKafkaMapper productVersionKafkaMapper = ProductVersionKafkaMapper.INSTANCE;

  private final ProductVersionsProperties productVersionsProperties;

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProductVersionEventKafkaProducer(ApplicationProperties applicationProperties,
      KafkaTemplate<String, Object> kafkaTemplate) {
	this.kafkaTemplate = kafkaTemplate;

    this.productVersionsProperties = applicationProperties.getProductVersions();
  }

  @Override
  public void send(ProductVersionEvent event) {
    String key = event.getEntityId().toString();
    var data = productVersionKafkaMapper.toDTO(event);

    kafkaTemplate.send(productVersionsProperties.getTopic(), key, data);
  }
}
