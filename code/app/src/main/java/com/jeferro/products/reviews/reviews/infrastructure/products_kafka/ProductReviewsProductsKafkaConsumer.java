package com.jeferro.products.reviews.reviews.infrastructure.products_kafka;

import com.jeferro.products.generated.kafka.v1.dtos.ProductDeletedAvroDTO;
import com.jeferro.products.reviews.reviews.application.params.DeleteAllProductReviewsOfProductParams;
import com.jeferro.products.products.products.infrastructure.kafka.mappers.ProductKafkaMapper;
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
        topics = "${components.products.products-topic}",
        groupId = "${components.products.product-reviews-consumer-group-id}"
)
public class ProductReviewsProductsKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ProductReviewsProductsKafkaConsumer.class);

    private final ProductKafkaMapper productKafkaMapper = ProductKafkaMapper.INSTANCE;

    private final UseCaseBus useCaseBus;

    @KafkaHandler
    protected void consume(ProductDeletedAvroDTO productDeletedAvroDTO) {
        var params = new DeleteAllProductReviewsOfProductParams(
                productKafkaMapper.toDomain(productDeletedAvroDTO.getVersionId())
        );

        useCaseBus.execute(params);
    }

    @KafkaHandler(isDefault = true)
    protected void consume(Object eventAvroDTO) {
        logger.debug("Ignoring event " + eventAvroDTO);
    }
}
