package com.db1.orders.entrypoint.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db1.orders.application.dto.CreateOrderResponse;
import com.db1.orders.application.usecase.CreateOrderUseCase;
import com.db1.orders.application.usecase.FindOrderByUseCase;
import com.db1.orders.domain.modal.Orders;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

        private final CreateOrderUseCase createOrderUseCase;
        private final FindOrderByUseCase findOrderByUseCase;

        @PostMapping
        public ResponseEntity<CreateOrderResponse> create(@RequestHeader("Idempotency-Key") String idempotencyKey,
                        @RequestBody Orders request) {
                var createdOrder = createOrderUseCase.execute(request, idempotencyKey);

                CreateOrderResponse response = new CreateOrderResponse(
                                createdOrder.getId(),
                                createdOrder.getOrderId(),
                                createdOrder.getStatus().getKey());

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{orderId}")
        public ResponseEntity<Orders> getById(@PathVariable String orderId) {
                Orders order = findOrderByUseCase.execute(orderId);

                if (order == null) {
                        return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(order);
        }

}