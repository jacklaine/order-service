package com.db1.orders.application.port.out;

import com.db1.orders.domain.modal.Orders;
import com.db1.orders.infra.messaging.kafka.dto.OrderEventPayload;

public interface OrderEventPublisher {

    void publishOrderCreated(Orders order, OrderEventPayload payload);
}
