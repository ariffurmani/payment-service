package org.furmani.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String currency;
    private Long totalAmount; // in smallest currency unit
    private List<OrderItem> items;
}

