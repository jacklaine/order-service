package com.db1.orders.application.usecase;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.db1.orders.application.dto.CreateOrderRequest;
import com.db1.orders.application.dto.OrderItemRequest;
import com.db1.orders.domain.interfaces.IOrderRepository;
import com.db1.orders.domain.modal.Orders;
import com.db1.orders.infra.AbstractIntegrationTest;

@SpringBootTest
@Transactional
class CreateOrderUseCaseTest extends AbstractIntegrationTest {

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private IOrderRepository orderRepository;

    @Test
    void shouldCreateOrderWithPendingStatus() {
        OrderItemRequest item = new OrderItemRequest("SN-001", "2", new BigDecimal("29.99"));
        CreateOrderRequest request = new CreateOrderRequest("CUST-1", "ORD-TEST-001", List.of(item));

        Orders saved = createOrderUseCase.execute(request, "idem-key-001");

        assertNotNull(saved);
        assertNotNull(saved.getId());

        Optional<Orders> found = orderRepository.findById(saved.getId());
        assertNotNull(found);
        assertNotNull(found.get().getOrderId());
    }

    @Test
    void shouldReturnSameOrderForDuplicateIdempotencyKey() {
        OrderItemRequest item = new OrderItemRequest("SN-002", "1", new BigDecimal("10.00"));
        CreateOrderRequest request1 = new CreateOrderRequest("CUST-2", "ORD-TEST-002", List.of(item));
        CreateOrderRequest request2 = new CreateOrderRequest("CUST-2", "ORD-TEST-002B", List.of(item));

        Orders first = createOrderUseCase.execute(request1, "idem-key-duplicate");
        Orders second = createOrderUseCase.execute(request2, "idem-key-duplicate");

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.getId(), second.getId());
    }
}
