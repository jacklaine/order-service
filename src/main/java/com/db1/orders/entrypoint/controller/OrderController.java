package com.db1.orders.entrypoint.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db1.orders.application.dto.CreateOrderRequest;
import com.db1.orders.application.dto.CreateOrderResponse;
import com.db1.orders.application.usecase.CreateOrderUseCase;
import com.db1.orders.application.usecase.FindOrderByUseCase;
import com.db1.orders.domain.modal.Orders;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

        private final CreateOrderUseCase createOrderUseCase;
        private final FindOrderByUseCase findOrderByUseCase;

        @PostMapping
        public ResponseEntity<CreateOrderResponse> create(@RequestHeader("Idempotency-Key") String idempotencyKey,
                        @Valid @RequestBody CreateOrderRequest request) {
                var createdOrder = createOrderUseCase.execute(request, idempotencyKey);

                CreateOrderResponse response = new CreateOrderResponse(
                                createdOrder.getId(),
                                createdOrder.getStatus().getKey());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Orders> getById(@PathVariable UUID id) {
                Orders order = findOrderByUseCase.execute(id);
                if (order != null) {
                        return ResponseEntity.ok(order);
                }
                
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

}