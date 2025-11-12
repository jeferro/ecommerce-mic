package com.jeferro.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.jeferro.shared.*", "com.jeferro.products.*"})
@ConfigurationPropertiesScan(basePackages = {"com.jeferro.shared.*", "com.jeferro.products.*"})
public class EcommerceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EcommerceApplication.class, args);
  }
}
