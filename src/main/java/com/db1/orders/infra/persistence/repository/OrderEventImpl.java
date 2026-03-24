package com.db1.orders.infra.persistence.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.db1.orders.domain.interfaces.IOrderEventRepository;
import com.db1.orders.domain.interfaces.SpringDataEventRepository;
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
        entity.setProcessed(false);

        repository.save(entity);
    }

    private String serialize(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}