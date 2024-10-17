package com.checkout.payment.gateway.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PaymentGatewayInputConverterTest {

  @Test
  void generateRejectedPaymentResponse() {
    PaymentGatewayInputConverter paymentGatewayInputConverter = new PaymentGatewayInputConverter();
    UUID uuid = UUID.fromString("1234-5678-9012-3456-78901");
    PostPaymentResponse postPaymentResponse = paymentGatewayInputConverter.generateRejectedPaymentResponse(uuid);

    assertEquals(uuid, postPaymentResponse.getId());
    assertEquals(PaymentStatus.REJECTED, postPaymentResponse.getStatus());
  }

  @Test
  void generateValidPostPaymentResponse() {
    PaymentGatewayInputConverter paymentGatewayInputConverter = new PaymentGatewayInputConverter();
    UUID uuid = UUID.fromString("1234-5678-9012-3456-78901");
    PostPaymentRequest input = new PostPaymentRequest();
    input.setAmount(100);
    input.setCurrency("USD");
    input.setExpiryMonth(12);
    input.setExpiryYear(2022);
    input.setCardNumber("876543210987654321");
    input.setCvv("451");

    PostPaymentResponse postPaymentResponse = paymentGatewayInputConverter.generateValidPostPaymentResponse(
        input,
        PaymentStatus.AUTHORIZED,
        uuid
    );

    assertEquals(uuid, postPaymentResponse.getId());
    assertEquals(PaymentStatus.AUTHORIZED, postPaymentResponse.getStatus());
    assertEquals(100, postPaymentResponse.getAmount());
    assertEquals("USD", postPaymentResponse.getCurrency());
    assertEquals(12, postPaymentResponse.getExpiryMonth());
    assertEquals(2022, postPaymentResponse.getExpiryYear());
    assertEquals("4321", postPaymentResponse.getCardNumberLastFour());
  }
}