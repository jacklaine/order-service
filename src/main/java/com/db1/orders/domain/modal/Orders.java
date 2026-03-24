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

    private Instant createdAt;

    private Instant updatedAt;

    public Orders(String customerId, String orderId, List<OrderItem> items) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.orderId = orderId;
        this.items = items;
        this.status = EnumOrderStatus.PENDING;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public EnumOrderStatus getStatus() {
        return status;
    }

    public void setStatus(EnumOrderStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

}
