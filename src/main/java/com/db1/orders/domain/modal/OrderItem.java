package com.db1.orders.domain.modal;

import java.math.BigDecimal;

public class OrderItem {

    private String sku;

    private int quantity;

    private BigDecimal unitPrice;

    public OrderItem(String sku, int quantity, BigDecimal unitPrice) {
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

}
