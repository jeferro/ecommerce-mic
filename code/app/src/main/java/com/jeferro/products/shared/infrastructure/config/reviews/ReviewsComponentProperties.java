package com.jeferro.products.shared.infrastructure.config.reviews;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("components.reviews")
public class ReviewsComponentProperties {

    private String productsTopic;

    private String productReviewsTopic;

    private String productReviewsConsumerGroupId;
}
