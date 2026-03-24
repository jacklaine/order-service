package com.db1.orders.domain.modal;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEvent {

    private UUID id;

    private String orderId;

    private String eventType;

    private String payload;

    private boolean processed;

    public OrderEvent(String orderId, String payload) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.eventType = "OrderCreated";
        this.payload = payload;
        this.processed = false;
    }

}
