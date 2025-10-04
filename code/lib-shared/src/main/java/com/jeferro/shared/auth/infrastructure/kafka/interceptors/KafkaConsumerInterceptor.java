package com.jeferro.shared.auth.infrastructure.kafka.interceptors;

import com.jeferro.shared.auth.infrastructure.ContextManager;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class KafkaConsumerInterceptor implements ConsumerInterceptor<String, String> {

  @Override
  public void configure(Map<String, ?> configs) {
    // Do nothing
  }

  @Override
  public ConsumerRecords<String, String> onConsume(
      ConsumerRecords<String, String> consumerRecords) {
    ContextManager.signInSystem();

    return consumerRecords;
  }

  @Override
  public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {
    // Do nothing
  }

  @Override
  public void close() {
    // Do nothing
  }
}
