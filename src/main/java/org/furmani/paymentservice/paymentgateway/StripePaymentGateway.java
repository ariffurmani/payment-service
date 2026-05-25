package org.furmani.paymentservice.paymentgateway;

import com.stripe.Stripe;
import com.stripe.model.PaymentLink;
import com.stripe.param.PaymentLinkCreateParams;
import org.furmani.paymentservice.dto.OrderItem;
import org.furmani.paymentservice.dto.OrderResponse;
import org.furmani.paymentservice.dto.PaymentLinkResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StripePaymentGateway implements PaymentGateway {

    @Value("${stripe.key:}")
    private String stripeKey;

    @Override
    public PaymentLinkResponse generatePaymentLink(OrderResponse order) {
        if (order == null) {
            throw new IllegalArgumentException("order must be provided");
        }

        String effectiveKey = stripeKey;
        if (effectiveKey == null || effectiveKey.isBlank()) {
            effectiveKey = System.getenv("STRIPE_KEY");
        }
        if (effectiveKey == null || effectiveKey.isBlank()) {
            throw new RuntimeException("Stripe API key is not configured. Set 'stripe.key' property or STRIPE_KEY env variable.");
        }
        Stripe.apiKey = effectiveKey;


        try {
            PaymentLinkCreateParams.Builder paramsBuilder = PaymentLinkCreateParams.builder();

            List<OrderItem> items = order.getItems();
            if (items != null && !items.isEmpty()) {
                for (OrderItem item : items) {
                    long unitAmount = item.getUnitAmount() != null ? item.getUnitAmount() : 0L;
                    long quantity = item.getQuantity() != null ? item.getQuantity() : 1L;

                    PaymentLinkCreateParams.LineItem.PriceData.ProductData productData =
                            PaymentLinkCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(item.getName() != null ? item.getName() : "Item")
                                    .setDescription(item.getDescription())
                                    .build();

                    PaymentLinkCreateParams.LineItem.PriceData priceData =
                            PaymentLinkCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(item.getCurrency() != null ? item.getCurrency() : (order.getCurrency() != null ? order.getCurrency() : "usd"))
                                    .setUnitAmount(unitAmount)
                                    .setProductData(productData)
                                    .build();

                    PaymentLinkCreateParams.LineItem lineItem = PaymentLinkCreateParams.LineItem.builder()
                            .setPriceData(priceData)
                            .setQuantity(quantity)
                            .build();

                    paramsBuilder.addLineItem(lineItem);
                }
            } else {
                // fallback to single line item using order total
                long total = order.getTotalAmount() != null ? order.getTotalAmount() : 0L;
                PaymentLinkCreateParams.LineItem.PriceData.ProductData productData =
                        PaymentLinkCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Order #" + (order.getId() != null ? order.getId().toString() : ""))
                                .build();

                PaymentLinkCreateParams.LineItem.PriceData priceData =
                        PaymentLinkCreateParams.LineItem.PriceData.builder()
                                .setCurrency(order.getCurrency() != null ? order.getCurrency() : "usd")
                                .setUnitAmount(total)
                                .setProductData(productData)
                                .build();

                PaymentLinkCreateParams.LineItem lineItem = PaymentLinkCreateParams.LineItem.builder()
                        .setPriceData(priceData)
                        .setQuantity(1L)
                        .build();

                paramsBuilder.addLineItem(lineItem);
            }

            PaymentLinkCreateParams params = paramsBuilder.build();

            PaymentLink link = PaymentLink.create(params);

            PaymentLinkResponse resp = new PaymentLinkResponse();
            resp.setUrl(link.getUrl());
            resp.setId(link.getId());
            resp.setCurrency(order.getCurrency());
            resp.setAmount(order.getTotalAmount());
            return resp;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe payment link", e);
        }
    }
}
