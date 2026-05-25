package org.furmani.paymentservice.controller;

import org.furmani.paymentservice.dto.PaymentLinkResponse;
import org.furmani.paymentservice.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/generatePaymentLink")
    public PaymentLinkResponse generatePaymentLink(@RequestParam Long orderId) {
        return paymentService.generatePaymentLink(orderId);
    }
}
