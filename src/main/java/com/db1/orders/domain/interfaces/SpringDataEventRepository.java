package com.db1.orders.domain.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.db1.orders.infra.persistence.entity.OrderEventEntity;

public interface SpringDataEventRepository extends JpaRepository<OrderEventEntity, UUID> {
}
