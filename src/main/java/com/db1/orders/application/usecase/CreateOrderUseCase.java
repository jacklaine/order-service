package com.db1.orders.application.usecase;

import org.springframework.stereotype.Service;

import com.db1.orders.domain.interfaces.IOrderRepository;
import com.db1.orders.domain.modal.Orders;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final IOrderRepository orderRepository;

    public void execute(Orders order) {
        orderRepository.save(order);
    }
}
