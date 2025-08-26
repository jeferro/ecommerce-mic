package com.jeferro.products.reviews.reviews.infrastructure.kafka;

import com.jeferro.products.reviews.reviews.domain.events.ReviewEvent;
import com.jeferro.products.reviews.reviews.infrastructure.kafka.mappers.ProductReviewKafkaMapper;
import com.jeferro.products.shared.infrastructure.config.products.ProductsComponentProperties;
import com.jeferro.shared.ddd.domain.events.EventBusProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReviewEventKafkaProducer implements EventBusProducer<ReviewEvent> {

    private final ProductReviewKafkaMapper productReviewKafkaMapper = ProductReviewKafkaMapper.INSTANCE;

    private final ProductsComponentProperties productsComponentProperties;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(ReviewEvent event) {
        String key = event.getReviewId().toString();
        var data = productReviewKafkaMapper.toDTO(event);

        kafkaTemplate.send(productsComponentProperties.getProductReviewsTopic(), key, data);
    }

}
