package com.db1.orders.infra.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.db1.orders.domain.interfaces.IOrderEventRepository;
import com.db1.orders.domain.modal.OrderEvent;
import com.db1.orders.infra.persistence.entity.OrderEventEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderEventImpl implements IOrderEventRepository {

    private final SpringDataEventRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(OrderEvent event) {

        OrderEventEntity entity = new OrderEventEntity();
        entity.setId(UUID.randomUUID());
        entity.setOrderId(event.getOrderId());
        entity.setEventType("OrderCreated");
        entity.setPayload(serialize(event));
        entity.setProcessed(event.isProcessed());

        repository.save(entity);
    }

    @Override
    public void markAsProcessed(String orderId) {
        repository.markAsProcessed(orderId, LocalDateTime.now());
    }

    private String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderEventEntity> findByProcessedFalse() {
        return repository.findByProcessedFalse();
    }
}