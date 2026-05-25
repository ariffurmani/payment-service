package org.furmani.paymentservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class OrderServiceConfig {

    @Value("${order.service.url:http://localhost:8080/order-service/orders/{orderId}}")
    private String orderUrl;
}

