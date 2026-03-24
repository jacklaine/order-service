package com.db1.orders.infra.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.db1.orders.domain.modal.OrderEvent;

@Configuration
public class KafkaConfig {

    public static final String TOPIC_ORDERS_EVENTS = "orders.v1.events";
    public static final String TOPIC_ORDERS_EVENTS_DLQ = "orders.v1.events.dlq";

    private static final long RETRY_INTERVAL_MS = 1000L;
    private static final long MAX_RETRIES = 3;

    @Bean
    public NewTopic ordersEventsTopic() {
        return TopicBuilder.name(TOPIC_ORDERS_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ordersEventsDlqTopic() {
        return TopicBuilder.name(TOPIC_ORDERS_EVENTS_DLQ)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, OrderEvent> consumerFactory,
            KafkaTemplate<String, OrderEvent> kafkaTemplate) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderEvent>();
        factory.setConsumerFactory(consumerFactory);

        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (msg, ex) -> new TopicPartition(TOPIC_ORDERS_EVENTS_DLQ, msg.partition()));

        var errorHandler = new DefaultErrorHandler(
                recoverer,
                new FixedBackOff(RETRY_INTERVAL_MS, MAX_RETRIES));

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}