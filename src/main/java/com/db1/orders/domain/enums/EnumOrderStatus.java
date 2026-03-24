package com.db1.orders.domain.enums;

public enum EnumOrderStatus {

    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    REJECTED("REJECTED");

    private EnumOrderStatus(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return key;
    }

}
