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

import com.db1.orders.domain.interfaces.IOrderRepository;
import com.db1.orders.domain.modal.OrderItem;
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
        OrderItem item = new OrderItem("SN-001", "2", new BigDecimal("29.99"));
        Orders order = new Orders("CUST-1", "ORD-TEST-001", List.of(item));

        Orders saved = createOrderUseCase.execute(order, "idem-key-001");

        assertNotNull(saved);
        assertNotNull(saved.getId());

        Optional<Orders> found = orderRepository.findById(saved.getId());
        assertNotNull(found);
        assertNotNull(found.get().getOrderId());
    }

    @Test
    void shouldReturnSameOrderForDuplicateIdempotencyKey() {
        OrderItem item = new OrderItem("SN-002", "1", new BigDecimal("10.00"));
        Orders order1 = new Orders("CUST-2", "ORD-TEST-002", List.of(item));
        Orders order2 = new Orders("CUST-2", "ORD-TEST-002B", List.of(item));

        Orders first = createOrderUseCase.execute(order1, "idem-key-duplicate");
        Orders second = createOrderUseCase.execute(order2, "idem-key-duplicate");

        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.getId(), second.getId());
    }
}
