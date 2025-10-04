package com.jeferro.products.products.infrastructure.properties;

import com.jeferro.products.products.domain.ProductsConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("application.configs")
public class ProductProperties extends ProductsConfig {}
