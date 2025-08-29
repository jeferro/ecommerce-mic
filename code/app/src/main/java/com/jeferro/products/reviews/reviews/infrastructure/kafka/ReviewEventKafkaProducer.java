package com.jeferro.products.reviews.reviews.infrastructure.kafka;

import com.jeferro.products.reviews.reviews.domain.events.ReviewEvent;
import com.jeferro.products.reviews.reviews.infrastructure.kafka.mappers.ReviewKafkaMapper;
import com.jeferro.products.shared.infrastructure.config.products.ProductsComponentProperties;
import com.jeferro.shared.ddd.domain.events.EventBusProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewEventKafkaProducer implements EventBusProducer<ReviewEvent> {

    private final ReviewKafkaMapper reviewKafkaMapper = ReviewKafkaMapper.INSTANCE;

    private final ProductsComponentProperties productsComponentProperties;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(ReviewEvent event) {
        String key = event.getReviewId().toString();
        var data = reviewKafkaMapper.toDTO(event);

        kafkaTemplate.send(productsComponentProperties.getProductsTopic(), key, data);
    }

}
