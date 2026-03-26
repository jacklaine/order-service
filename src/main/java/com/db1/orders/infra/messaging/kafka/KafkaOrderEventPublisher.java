package com.db1.orders.infra.messaging.kafka;

import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.db1.orders.application.port.out.OrderEventPublisher;
import com.db1.orders.domain.modal.Orders;
import com.db1.orders.infra.config.KafkaConfig;
import com.db1.orders.infra.messaging.kafka.dto.OrderEventEnvelope;
import com.db1.orders.infra.messaging.kafka.dto.OrderEventPayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private final KafkaTemplate<String, OrderEventEnvelope> kafkaTemplate;

    @Override
    public void publishOrderCreated(Orders order, OrderEventPayload payload) {

        var orderId = order.getId().toString();

        var event = new OrderEventEnvelope();
        event.setType("OrderCreated");
        event.setPayload(payload);

        kafkaTemplate.send(KafkaConfig.TOPIC_ORDERS_EVENTS, orderId, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Erro ao publicar evento. topic={}, orderId={}, event={}",
                                KafkaConfig.TOPIC_ORDERS_EVENTS, orderId, event, ex);
                        return;
                    }

                    log.info("Evento publicado com sucesso. topic={}, orderId={}, event={}",
                            KafkaConfig.TOPIC_ORDERS_EVENTS, orderId, event);
                });
    }
}