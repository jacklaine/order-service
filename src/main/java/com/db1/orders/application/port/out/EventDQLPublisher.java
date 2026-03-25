package com.db1.orders.application.port.out;

import com.db1.orders.infra.persistence.entity.OrderEventEntity;

public interface EventDQLPublisher {
    void sendToDLQ(OrderEventEntity event, Exception error);
}
