package com.db1.orders.infra.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "idempotency_key")
@Getter
@Setter
@NoArgsConstructor
public class IdempotencyKeyEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private UUID orderId;

    @Column(updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    public IdempotencyKeyEntity(String idempotencyKey, UUID orderId) {
        this.idempotencyKey = idempotencyKey;
        this.orderId = orderId;
    }
}
