package com.jeferro.products.shared.infrastructure.config.products;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("components.products")
public class ProductsComponentProperties {

  private String topic;

  private String productReviewsConsumerGroupId;
}
