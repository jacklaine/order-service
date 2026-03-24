package com.db1.orders.domain.interfaces;

import com.db1.orders.domain.modal.OrderEvent;

public interface IOrderEventRepository {

    void save(OrderEvent event);

    void markAsProcessed(String orderId);

}
