package com.db1.orders.infra.messaging.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DQLMessage {

    private String orderId;

    private String eventType;

    private String payload;

    private String errorMessage;

    private String errorType;

    private long timestamp;

}
