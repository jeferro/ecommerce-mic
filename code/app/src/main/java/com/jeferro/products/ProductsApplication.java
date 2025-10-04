package com.jeferro.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.jeferro.shared.*", "com.jeferro.products.*"})
@ConfigurationPropertiesScan(basePackages = {"com.jeferro.shared.*", "com.jeferro.products.*"})
public class ProductsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductsApplication.class, args);
  }
}
