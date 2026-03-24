package com.db1.orders.domain.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.db1.orders.domain.modal.Orders;

public interface IOrderRepository {

    Orders save(Orders order);

    Optional<Orders> findById(UUID id);

    Orders findByOrderId(String orderId);

    void updateStatus(String orderId, String status, String reason);
}