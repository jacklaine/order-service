package com.db1.orders.entrypoint.consumer;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.db1.orders.application.usecase.UpdateOrderStatusUseCase;
import com.db1.orders.infra.config.KafkaConfig;
import com.db1.orders.infra.messaging.kafka.dto.OrderEventEnvelope;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @KafkaListener(
        topics = KafkaConfig.TOPIC_ORDERS_EVENTS,
        groupId = "order-service"
    )
    public void consume(OrderEventEnvelope event) {

        if (event == null || event.getPayload() == null) {
            log.warn("Evento inválido recebido: {}", event);
            return;
        }

        var orderIdStr = event.getPayload().getOrderId();

        if (orderIdStr == null) {
            log.warn("Evento sem orderId: {}", event);
            return;
        }

        var orderId = UUID.fromString(orderIdStr);
        handle(event, orderId);
    }

    private void handle(OrderEventEnvelope event, UUID orderId) {
        switch (event.getType()) {

            case "OrderConfirmed" -> confirm(orderId);

            case "OrderRejected" -> reject(orderId, event.getPayload().getReason());

            case "OrderCreated" -> 
                log.debug("Ignorando OrderCreated. orderId={}", orderId);

            default -> 
                log.warn("Evento não suportado: type={}, orderId={}", event.getType(), orderId);
        }
    }

    private void confirm(UUID orderId) {
        log.info("Confirmando pedido. orderId={}", orderId);
        updateOrderStatusUseCase.confirm(orderId);
    }

    private void reject(UUID orderId, String reason) {
        var safeReason = reason != null ? reason : "Sem motivo";
        log.info("Rejeitando pedido. orderId={}, reason={}", orderId, safeReason);
        updateOrderStatusUseCase.reject(orderId, safeReason);
    }
}