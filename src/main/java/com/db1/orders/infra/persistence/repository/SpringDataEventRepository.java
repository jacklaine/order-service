package com.db1.orders.infra.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.db1.orders.infra.persistence.entity.OrderEventEntity;

import jakarta.transaction.Transactional;

public interface SpringDataEventRepository extends JpaRepository<OrderEventEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE OrderEventEntity e SET e.processed = true, e.processedAt = :now WHERE e.orderId = :orderId")
    void markAsProcessed(@Param("orderId") String orderId, @Param("now") LocalDateTime now);

    List<OrderEventEntity> findByProcessedFalse();
}
