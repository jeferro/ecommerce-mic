package com.jeferro.ecommerce.shared.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("components.reviews")
public class ReviewsProperties {

  private String productsTopic;

  private String productReviewsTopic;

  private String productReviewsConsumerGroupId;
}
