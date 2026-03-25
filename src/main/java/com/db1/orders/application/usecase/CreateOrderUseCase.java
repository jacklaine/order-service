package com.db1.orders.application.usecase;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.db1.orders.application.dto.CreateOrderRequest;
import com.db1.orders.application.dto.OrderItemRequest;
import com.db1.orders.application.port.out.OrderEventPublisher;
import com.db1.orders.domain.interfaces.IOrderEventRepository;
import com.db1.orders.domain.interfaces.IOrderRepository;
import com.db1.orders.domain.modal.OrderEvent;
import com.db1.orders.domain.modal.OrderItem;
import com.db1.orders.domain.modal.Orders;
import com.db1.orders.infra.messaging.idempotency.IdempotencyKeyManager;
import com.db1.orders.infra.messaging.kafka.dto.OrderEventPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final IOrderRepository orderRepository;
    private final IOrderEventRepository orderEventRepository;
    private final OrderEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate transactionTemplate;
    private final IdempotencyKeyManager idempotencyKeyManager;

    @Transactional
    public Orders execute(CreateOrderRequest request, String idempotencyKey) {
        var existingOrderId = idempotencyKeyManager.getOrderIdForKey(idempotencyKey);
        if (existingOrderId != null) {
            log.info("Requisição duplicada detectada para a chave de idempotência: {}", idempotencyKey);
            return orderRepository.findByOrderId(existingOrderId);
        }

        var order = toDomain(request);

        var savedOrder = transactionTemplate.execute(status -> {
            var s = orderRepository.save(order);
            idempotencyKeyManager.storeIdempotencyKey(idempotencyKey, s.getOrderId());

            String payloadJson;
            try {
                payloadJson = objectMapper.writeValueAsString(buildPayload(s));
            } catch (JsonProcessingException e) {
                log.error("Erro ao serializar payload. orderId={}", order.getOrderId(), e);
                status.setRollbackOnly();
                throw new RuntimeException("Erro ao serializar payload", e);
            }

            orderEventRepository.save(new OrderEvent(s.getOrderId(), payloadJson));
            return s;
        });

        // Kafka chamado após commit confirmado, fora do contexto da transação
        try {
            eventPublisher.publishOrderCreated(savedOrder, buildPayload(savedOrder));
            orderEventRepository.markAsProcessed(savedOrder.getOrderId());
        } catch (Exception e) {
            log.warn("Falha ao publicar evento. orderId={}.", savedOrder.getOrderId(), e);
        }

        return savedOrder;
    }

    private Orders toDomain(CreateOrderRequest request) {
        var items = request.getItems().stream()
                .map(this::toDomainItem)
                .collect(Collectors.toList());
        return new Orders(null, request.getCustomerId(), request.getOrderId(), items, null, null);
    }

    private OrderItem toDomainItem(OrderItemRequest itemRequest) {
        return new OrderItem(itemRequest.getSku(), itemRequest.getQuantity(), itemRequest.getUnitPrice());
    }

    private OrderEventPayload buildPayload(Orders order) {
        var payload = new OrderEventPayload();
        payload.setOrderId(order.getOrderId());
        payload.setCustomerId(order.getCustomerId());
        payload.setItems(order.getItems());
        payload.setCreatedAt(LocalDateTime.now().toString());
        return payload;
    }
}
