package com.db1.orders.infra.messaging.outbox;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.db1.orders.domain.interfaces.IOrderEventRepository;
import com.db1.orders.infra.config.KafkaConfig;
import com.db1.orders.infra.messaging.kafka.KafkaEventDLQPublisher;
import com.db1.orders.infra.messaging.kafka.dto.OrderEventEnvelope;
import com.db1.orders.infra.messaging.kafka.dto.OrderEventPayload;
import com.db1.orders.infra.persistence.entity.OrderEventEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobOutboxRelay {

    private final IOrderEventRepository orderEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final KafkaEventDLQPublisher dlqPublisher;

    @Scheduled(fixedDelayString = "${outbox.relay.fixed-delay-ms:5000}")
    @Transactional
    public void processOutbox() {
        List<OrderEventEntity> unprocessed = orderEventRepository.findByProcessedFalse();

        for (OrderEventEntity event : unprocessed) {
            try {
                OrderEventPayload payload = objectMapper.readValue(event.getPayload(), OrderEventPayload.class);

                OrderEventEnvelope envelope = new OrderEventEnvelope(event.getEventType(), payload);
                kafkaTemplate.send(KafkaConfig.TOPIC_ORDERS_EVENTS, event.getOrderId(), envelope).get();
                orderEventRepository.markAsProcessed(event.getOrderId());
                log.info("JOB publicou o evento {} do orderId={}", event.getEventType(), event.getOrderId());
            } catch (Exception e) {
                log.error("JOB falhou ao publicar o evento id ={}: {}", event.getId(), e.getMessage());
                dlqPublisher.sendToDLQ(event, e);
            }
        }
    }
}