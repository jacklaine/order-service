package com.db1.orders.entrypoint.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db1.orders.application.dto.CreateOrderResponse;
import com.db1.orders.application.usecase.CreateOrderUseCase;
import com.db1.orders.domain.modal.Orders;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase useCase;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> create(@RequestBody Orders request) {
        var createdOrder = useCase.execute(request);
        var saved = new CreateOrderResponse(createdOrder.getId(), createdOrder.getOrderId(),
                createdOrder.getStatus().getKey());

        CreateOrderResponse response = new CreateOrderResponse(
                saved.getId(),
                saved.getOrderId(),
                saved.getStatus());

        return ResponseEntity.ok(response);
    }

}