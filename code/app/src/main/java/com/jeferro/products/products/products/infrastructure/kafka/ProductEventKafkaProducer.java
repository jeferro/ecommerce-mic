package com.jeferro.products.products.products.infrastructure.kafka;

import com.jeferro.products.products.products.domain.events.ProductEvent;
import com.jeferro.products.products.products.infrastructure.kafka.mappers.ProductKafkaMapper;
import com.jeferro.products.shared.infrastructure.config.products.ProductsComponentProperties;
import com.jeferro.shared.ddd.domain.events.EventBusProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventKafkaProducer implements EventBusProducer<ProductEvent> {

    private final ProductKafkaMapper productKafkaMapper = ProductKafkaMapper.INSTANCE;

    private final ProductsComponentProperties productsComponentProperties;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(ProductEvent event) {
        String key = event.getId().toString();
        var data = productKafkaMapper.toDTO(event);

        kafkaTemplate.send(productsComponentProperties.getProductsTopic(), key, data);
    }

}
