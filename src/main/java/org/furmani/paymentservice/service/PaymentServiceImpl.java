package org.furmani.paymentservice.service;

import org.furmani.paymentservice.config.OrderServiceConfig;
import org.furmani.paymentservice.dto.OrderResponse;
import org.furmani.paymentservice.dto.PaymentLinkResponse;
import org.furmani.paymentservice.exceptions.OrderNotFoundException;
import org.furmani.paymentservice.exceptions.PaymentException;
import org.furmani.paymentservice.paymentgateway.PaymentGateway;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentGateway paymentGateway;
    private final RestTemplate restTemplate;
    private final OrderServiceConfig orderServiceConfig;

    public PaymentServiceImpl(PaymentGateway paymentGateway, RestTemplate restTemplate, OrderServiceConfig orderServiceConfig) {
        this.paymentGateway = paymentGateway;
        this.restTemplate = restTemplate;
        this.orderServiceConfig = orderServiceConfig;
    }

    @Override
    public PaymentLinkResponse generatePaymentLink(Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("orderId must be a positive number");
        }

        OrderResponse order;
        try {
            order = restTemplate.getForObject(orderServiceConfig.getOrderUrl(), OrderResponse.class, orderId);
        } catch (RestClientException e) {
            throw new PaymentException("Failed to fetch order details for id: " + orderId, e);
        }

        if (order == null) {
            throw new OrderNotFoundException("Order not found: " + orderId);
        }

        try {
            return paymentGateway.generatePaymentLink(order);
        } catch (Exception e) {
            throw new PaymentException("Failed to generate payment link for order: " + orderId, e);
        }
    }
}
