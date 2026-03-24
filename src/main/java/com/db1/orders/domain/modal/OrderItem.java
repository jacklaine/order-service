package com.db1.orders.domain.modal;

import java.math.BigDecimal;

public class OrderItem {

    private String serialNumber;

    private String quantity;

    private BigDecimal unitPrice;

    public OrderItem(String serialNumber, String quantity, BigDecimal unitPrice) {
        this.serialNumber = serialNumber;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

}
