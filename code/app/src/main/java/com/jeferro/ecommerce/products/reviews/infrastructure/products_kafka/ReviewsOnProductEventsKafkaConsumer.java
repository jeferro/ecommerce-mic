package com.jeferro.ecommerce.products.reviews.infrastructure.products_kafka;

import com.jeferro.ecommerce.products.product_versions.infrastructure.kafka.dtos.ProductDeletedV1AvroDTO;
import com.jeferro.ecommerce.products.reviews.infrastructure.products_kafka.mappers.ReviewKafkaMapper;
import com.jeferro.shared.ddd.application.UseCaseBus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(
	topics = "${application.product-versions.topic}",
	groupId = "${application.reviews.product-versions-consumer-group-id}")
public class ReviewsOnProductEventsKafkaConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ReviewsOnProductEventsKafkaConsumer.class);

  private final ReviewKafkaMapper reviewKafkaMapper = ReviewKafkaMapper.INSTANCE;

  private final UseCaseBus useCaseBus;

  @KafkaHandler
  protected void consume(ProductDeletedV1AvroDTO productDeletedAvroDTO) {
	var params = reviewKafkaMapper.toDeleteAllReviewsOfEntityIdParams("products", productDeletedAvroDTO.getEntity());

	useCaseBus.executeWithRetry(params);
  }

  @KafkaHandler(isDefault = true)
  protected void consume(Object eventAvroDTO) {
	logger.debug("Ignoring event {}", eventAvroDTO);
  }
}
