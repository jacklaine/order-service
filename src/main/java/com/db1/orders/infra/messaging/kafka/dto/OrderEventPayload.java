package com.db1.orders.infra.messaging.kafka.dto;

import java.util.List;

import com.db1.orders.domain.modal.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventPayload {

    private String orderId;

    private String customerId;

    private List<OrderItem> items;

    private String reason;

    private String createdAt;

}