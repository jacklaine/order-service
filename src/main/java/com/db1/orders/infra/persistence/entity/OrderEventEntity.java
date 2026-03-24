package com.db1.orders.infra.persistence.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_event")
@Getter
@Setter
public class OrderEventEntity {

    @Id
    private UUID id;

    private String orderId;

    private String eventType;

    @Column(columnDefinition = "JSONB")
    private String payload;

    private boolean processed;

}