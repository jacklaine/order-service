package com.db1.orders.infra.persistence.mapper;

import org.springframework.stereotype.Component;

import com.db1.orders.domain.modal.OrderEvent;
import com.db1.orders.infra.persistence.entity.OrderEventEntity;

@Component
public class OrderEventMapper {

    public OrderEventEntity toEntity(OrderEvent order) {
        OrderEventEntity entity = new OrderEventEntity();
        entity.setId(order.getId());
        entity.setOrderId(order.getOrderId());
        entity.setEventType(order.getEventType());
        entity.setPayload(order.getPayload());

        return entity;
    }

    public OrderEvent toDomain(OrderEventEntity entity) {
        return new OrderEvent(entity.getOrderId(), entity.getPayload());
    }

}