package org.furmani.paymentservice.service;

import org.furmani.paymentservice.dto.PaymentLinkResponse;

public interface PaymentService {
    PaymentLinkResponse generatePaymentLink(Long orderId);
}
