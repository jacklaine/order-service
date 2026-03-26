package com.db1.orders.infra.persistence.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String sku;

    private int quantity;

    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrdersEntity order;

}
