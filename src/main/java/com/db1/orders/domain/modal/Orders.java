package com.db1.orders.domain.modal;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.db1.orders.domain.enums.EnumOrderStatus;

public class Orders {

    private UUID id;

    private String orderId;

    private String customerId;

    private List<OrderItem> items;

    private EnumOrderStatus status;

    private String reason;

    public Orders(UUID id, String orderId, String customerId, List<OrderItem> items, EnumOrderStatus status,
            String reason) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.status = status;
        this.reason = reason;
    }

    public void confirm() {
        this.status = EnumOrderStatus.CONFIRMED;
    }

    public void reject(String reason) {
        this.status = EnumOrderStatus.REJECTED;
        this.reason = reason;
    }

    public UUID getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public EnumOrderStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

}
