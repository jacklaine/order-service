package com.db1.orders.infra.messaging.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.db1.orders.application.port.out.EventDQLPublisher;
import com.db1.orders.infra.messaging.kafka.dto.DQLMessage;
import com.db1.orders.infra.persistence.entity.OrderEventEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventDLQPublisher implements EventDQLPublisher {

    private static final String DLQ_TOPIC = "orders.v1.events.dlq";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendToDLQ(OrderEventEntity event, Exception error) {

        var orderId = event.getOrderId().toString();
        var eventType = event.getEventType();
        var payload = event.getPayload();

        try {
            var dlqMessage = new DQLMessage(
                    orderId,
                    eventType,
                    objectMapper.writeValueAsString(payload),
                    error.getMessage(),
                    error.getClass().getSimpleName(),
                    System.currentTimeMillis()
            );

            var dlqPayload = objectMapper.writeValueAsString(dlqMessage);

            kafkaTemplate.send(DLQ_TOPIC, orderId, dlqPayload)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Erro ao publicar evento na DLQ. topic={}, orderId={}, event={}", DLQ_TOPIC, orderId, dlqMessage, ex);
                            return;
                        }

                        log.info("Evento publicado com sucesso na DLQ. topic={}, orderId={}, event={}", DLQ_TOPIC, orderId, dlqMessage);
                    });

        } catch (Exception ex) {
            log.error("Falha ao montar/enviar evento para DLQ. topic={}, orderId={}, event={}", DLQ_TOPIC, orderId, event, ex
            );
        }
    }
}