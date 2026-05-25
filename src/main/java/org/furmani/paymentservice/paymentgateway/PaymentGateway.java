package org.furmani.paymentservice.paymentgateway;

import org.furmani.paymentservice.dto.OrderResponse;
import org.furmani.paymentservice.dto.PaymentLinkResponse;

public interface PaymentGateway {
    PaymentLinkResponse generatePaymentLink(OrderResponse order);
}
