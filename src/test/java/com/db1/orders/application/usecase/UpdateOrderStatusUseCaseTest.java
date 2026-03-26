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
        CreateOrderRequest order = new CreateOrderRequest("CUST-001", List.of(item));
        Orders created = createOrderUseCase.execute(order, "idem-001");

        updateOrderStatusUseCase.confirm(created.getId());

        Orders updated = orderRepository.findById(created.getId()).orElse(null);
        assertEquals(EnumOrderStatus.CONFIRMED, updated.getStatus());
    }

    @Test
    void shouldRejectOrder() {
        OrderItemRequest item = new OrderItemRequest("SN-002", 1, new BigDecimal("50.00"));
        CreateOrderRequest order = new CreateOrderRequest("CUST-002", List.of(item));
        Orders created = createOrderUseCase.execute(order, "idem-002");

        updateOrderStatusUseCase.reject(created.getId(), "Estoque baixo");

        Orders updated = orderRepository.findById(created.getId()).orElse(null);
        assertEquals(EnumOrderStatus.REJECTED, updated.getStatus());
        assertEquals("Estoque baixo", updated.getReason());
    }
}
