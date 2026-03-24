package com.db1.orders.infra.persistence.mapper;

import org.springframework.stereotype.Component;

import com.db1.orders.domain.modal.OrderItem;
import com.db1.orders.infra.persistence.entity.OrderItemEntity;
import com.db1.orders.infra.persistence.entity.OrdersEntity;

@Component
public class OrderItemMapper {

    public static OrderItemEntity toEntity(OrderItem item, OrdersEntity order) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setSerialNumber(item.getSerialNumber());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setOrder(order);
        return entity;
    }

    public static OrderItem toDomain(OrderItemEntity entity) {
        return new OrderItem(entity.getSerialNumber(), entity.getQuantity(), entity.getUnitPrice());
    }
}