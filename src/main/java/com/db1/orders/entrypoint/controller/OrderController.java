package com.db1.orders.entrypoint.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db1.orders.application.usecase.CreateOrderUseCase;
import com.db1.orders.domain.modal.Orders;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase useCase;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Orders request) {
        useCase.execute(request);
        return ResponseEntity.noContent().build();
    }
}