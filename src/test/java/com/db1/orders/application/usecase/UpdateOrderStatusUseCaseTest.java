package com.db1.orders.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.db1.orders.application.dto.CreateOrderRequest;
import com.db1.orders.application.dto.OrderItemRequest;
import com.db1.orders.domain.enums.EnumOrderStatus;
import com.db1.orders.domain.interfaces.IOrderRepository;
import com.db1.orders.domain.modal.Orders;
import com.db1.orders.infra.AbstractIntegrationTest;

@SpringBootTest
@Transactional
class UpdateOrderStatusUseCaseTest extends AbstractIntegrationTest {

    @Autowired
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Autowired
    private IOrderRepository orderRepository;

    @Test
    void shouldConfirmOrder() {
        OrderItemRequest item = new OrderItemRequest("SN-001", 1, new BigDecimal("50.00"));
        CreateOrderRequest order = new CreateOrderRequest("CUST-001", "ORD-001", List.of(item));
        createOrderUseCase.execute(order, "idem-001");

        updateOrderStatusUseCase.confirm("ORD-001");

        Orders updated = orderRepository.findByOrderId("ORD-001");
        assertEquals(EnumOrderStatus.CONFIRMED, updated.getStatus());
    }

    @Test
    void shouldRejectOrder() {
        OrderItemRequest item = new OrderItemRequest("SN-002", 1, new BigDecimal("50.00"));
        CreateOrderRequest order = new CreateOrderRequest("CUST-002", "ORD-002", List.of(item));
        createOrderUseCase.execute(order, "idem-002");

        updateOrderStatusUseCase.reject("ORD-002", "Estoque baixo");

        Orders updated = orderRepository.findByOrderId("ORD-002");
        assertEquals(EnumOrderStatus.REJECTED, updated.getStatus());
        assertEquals("Estoque baixo", updated.getReason());
    }
}
