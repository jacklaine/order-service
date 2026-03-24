package com.db1.orders.infra.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.db1.orders.domain.interfaces.IOrderRepository;
import com.db1.orders.domain.modal.Orders;
import com.db1.orders.infra.persistence.mapper.OrdersMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements IOrderRepository {

    private final SpringDataOrderRepository jpaRepository;

    @Override
    public Orders save(Orders order) {
        return OrdersMapper.toDomain(
                jpaRepository.save(OrdersMapper.toEntity(order)));
    }

    @Override
    public Optional<Orders> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(OrdersMapper::toDomain);
    }

    @Override
    public Orders findByOrderId(String orderId) {
        return jpaRepository.findByOrderId(orderId)
                .map(OrdersMapper::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}