package com.db1.orders.infra.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.db1.orders.infra.persistence.entity.OrdersEntity;

public interface SpringDataOrderRepository extends JpaRepository<OrdersEntity, UUID> {

    Optional<OrdersEntity> findByOrderId(String orderId);
}
