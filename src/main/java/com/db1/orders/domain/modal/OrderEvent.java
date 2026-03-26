package com.db1.orders.domain.modal;

import java.util.UUID;

public class OrderEvent {

    private UUID id;

    private UUID orderId;

    private String eventType;

    private String payload;

    private boolean processed;

    public OrderEvent(UUID orderId, String payload) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.eventType = "OrderCreated";
        this.payload = payload;
        this.processed = false;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isProcessed() {
        return processed;
    }

}
