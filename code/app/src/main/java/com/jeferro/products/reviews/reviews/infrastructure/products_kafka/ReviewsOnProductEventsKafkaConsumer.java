package com.jeferro.products.reviews.reviews.infrastructure.products_kafka;

import com.jeferro.products.generated.kafka.v1.dtos.ProductDeletedAvroDTO;
import com.jeferro.products.reviews.reviews.application.params.DeleteAllReviewsOfEntityIdParams;
import com.jeferro.products.reviews.reviews.infrastructure.products_kafka.mappers.ReviewKafkaMapper;
import com.jeferro.shared.ddd.application.bus.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = "${components.products.topic}",
        groupId = "${components.reviews.reviews-on-product-events-consumer-group-id}"
)
public class ReviewsOnProductEventsKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ReviewsOnProductEventsKafkaConsumer.class);

    private final ReviewKafkaMapper reviewKafkaMapper = ReviewKafkaMapper.INSTANCE;

    private final UseCaseBus useCaseBus;

    @KafkaHandler
    protected void consume(ProductDeletedAvroDTO productDeletedAvroDTO) {
        var params = new DeleteAllReviewsOfEntityIdParams(
                reviewKafkaMapper.toDomain("products", productDeletedAvroDTO.getVersionId())
        );

        useCaseBus.execute(params);
    }

    @KafkaHandler(isDefault = true)
    protected void consume(Object eventAvroDTO) {
	  logger.debug("Ignoring event {}", eventAvroDTO);
    }
}
