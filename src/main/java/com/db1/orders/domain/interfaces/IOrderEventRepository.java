package com.db1.orders.domain.interfaces;

import java.util.List;

import com.db1.orders.domain.modal.OrderEvent;
import com.db1.orders.infra.persistence.entity.OrderEventEntity;

public interface IOrderEventRepository {

    void save(OrderEvent event);

    void markAsProcessed(String orderId);

    List<OrderEventEntity> findByProcessedFalse();

}
