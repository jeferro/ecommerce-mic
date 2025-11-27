package com.jeferro.ecommerce.shared.infrastructure.properties;

import com.jeferro.ecommerce.shared.domain.models.ApplicationConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("application")
public class ApplicationProperties extends ApplicationConfig {

  private ProductVersionsProperties productVersions;

  private ReviewsProperties reviews;
}
