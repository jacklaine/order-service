package com.db1.orders.infra.messaging.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventEnvelope {

    private String type;
    
    private OrderEventPayload payload;
}
