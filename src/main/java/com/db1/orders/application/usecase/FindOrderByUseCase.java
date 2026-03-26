package com.db1.orders.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.db1.orders.domain.interfaces.IOrderRepository;
import com.db1.orders.domain.modal.Orders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindOrderByUseCase {

    private final IOrderRepository orderRepository;

    public Orders execute(UUID id) {
        return orderRepository.findById(id).orElse(null);
    }

}
