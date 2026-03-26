package com.db1.orders.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderResponse {

    private UUID id;

    private String status;
    
}
