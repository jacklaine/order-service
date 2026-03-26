package com.db1.orders.infra.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.db1.orders.domain.enums.EnumOrderStatus;
import com.db1.orders.domain.modal.OrderItem;
import com.db1.orders.domain.modal.Orders;
import com.db1.orders.infra.persistence.entity.OrderItemEntity;
import com.db1.orders.infra.persistence.entity.OrdersEntity;

@Component
public class OrdersMapper {

    public static OrdersEntity toEntity(Orders order) {
        OrdersEntity entity = new OrdersEntity();
        entity.setCustomerId(order.getCustomerId());
        entity.setStatus(order.getStatus().getKey());
        entity.setReason(order.getReason());

        if (order.getItems() != null) {
            List<OrderItemEntity> items = new ArrayList<>(order.getItems().stream()
                    .map(item -> OrderItemMapper.toEntity(item, entity))
                    .toList());
            entity.setItems(items);
        }

        return entity;
    }

    public static Orders toDomain(OrdersEntity entity) {
        List<OrderItem> items = null;

        if (entity.getItems() != null) {
            items = entity.getItems().stream()
                    .map(OrderItemMapper::toDomain)
                    .toList();
        }

        return new Orders(
                entity.getId(),
                entity.getCustomerId(),
                items,
                EnumOrderStatus.valueOf(entity.getStatus()),
                entity.getReason());

    }
}