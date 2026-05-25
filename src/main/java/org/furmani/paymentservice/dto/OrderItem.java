package org.furmani.paymentservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItem {
    private String name;
    private String description;
    private Long unitAmount; // in smallest currency unit (e.g., cents)
    private Integer quantity;
    private String currency;
}

