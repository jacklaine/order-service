package com.db1.orders.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.db1.orders.domain.interfaces.IOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {

    private final IOrderRepository orderRepository;

    @Transactional
    public void confirm(String orderId) {
        orderRepository.updateStatus(orderId, "CONFIRMED", null);
        log.info("OrderConfirmed: {}", orderId);
    }

    @Transactional
    public void reject(String orderId, String reason) {
        orderRepository.updateStatus(orderId, "REJECTED", reason);
        log.info("OrderRejected: {}, Reason: {}", orderId, reason);
    }
}
