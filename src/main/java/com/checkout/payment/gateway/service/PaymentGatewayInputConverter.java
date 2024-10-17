package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import java.util.UUID;

public class PaymentGatewayInputConverter {

  PostPaymentResponse generateRejectedPaymentResponse(UUID uuid) {
    PostPaymentResponse response = new PostPaymentResponse();
    response.setId(uuid);
    response.setStatus(PaymentStatus.REJECTED);
    return response;
  }

  PostPaymentResponse generateValidPostPaymentResponse(PostPaymentRequest request,
      PaymentStatus paymentStatus, UUID uuid) {
    PostPaymentResponse response = new PostPaymentResponse();
    response.setId(uuid);
    response.setStatus(paymentStatus);
    response.setAmount(request.getAmount());
    response.setCurrency(request.getCurrency());
    response.setExpiryMonth(request.getExpiryMonth());
    response.setExpiryYear(request.getExpiryYear());
    response.setCardNumberLastFour(
        extractPaymentLastFour(request.getCardNumber()));
    return response;
  }

  private static String extractPaymentLastFour(String cardNumber) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      stringBuilder.append(cardNumber.charAt(cardNumber.length() - 1 - i));
    }
    return stringBuilder.reverse().toString();
  }
}