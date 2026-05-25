package org.furmani.paymentservice.exceptions;

public class OrderNotFoundException extends PaymentException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}

