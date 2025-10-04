package com.jeferro.shared.auth.infrastructure.kafka.configurations;

import static org.apache.kafka.clients.consumer.ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG;

import com.jeferro.shared.auth.infrastructure.kafka.interceptors.KafkaConsumerInterceptor;
import com.jeferro.shared.auth.infrastructure.kafka.interceptors.KafkaProducerInterceptor;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {

  @Bean
  public ConsumerFactory<?, ?> kafkaConsumerFactory(
      KafkaProperties properties, ObjectProvider<SslBundles> sslBundles) {
    Map<String, Object> consumerProperties =
        properties.buildConsumerProperties(sslBundles.getIfAvailable());

    consumerProperties.put(INTERCEPTOR_CLASSES_CONFIG, KafkaConsumerInterceptor.class.getName());

    return new DefaultKafkaConsumerFactory<>(consumerProperties);
  }

  @Bean
  public ProducerFactory<?, ?> kafkaProducerFactory(
      KafkaProperties properties, ObjectProvider<SslBundles> sslBundles) {
    Map<String, Object> producerProperties =
        properties.buildProducerProperties(sslBundles.getIfAvailable());

    producerProperties.put(INTERCEPTOR_CLASSES_CONFIG, KafkaProducerInterceptor.class.getName());

    return new DefaultKafkaProducerFactory<>(producerProperties);
  }
}
