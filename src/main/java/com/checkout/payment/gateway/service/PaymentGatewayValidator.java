package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Set;

public class PaymentGatewayValidator {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayValidator.class);

  private static final Set<String> CURRENCY_ALLOW_LIST = Set.of("USD", "EUR", "GBP");

  boolean isRequestValid(PostPaymentRequest paymentRequest) {
    if (paymentRequest.getAmount() < 0) {
      LOG.error("Invalid amount {}", paymentRequest.getAmount());
      return false;
    }

    if (paymentRequest.getExpiryMonth() <= 0 || paymentRequest.getExpiryMonth() > 12) {
      LOG.error("Invalid expiry month {}", paymentRequest.getExpiryMonth());
      return false;
    }

    if (paymentRequest.getExpiryYear() < LocalDate.now().getYear()) {
      LOG.error("Invalid expiry year {}", paymentRequest.getExpiryYear());
      return false;
    }

    if (!CURRENCY_ALLOW_LIST.contains(paymentRequest.getCurrency())) {
      LOG.error("Currency provided not supported {}", paymentRequest.getCurrency());
      return false;
    }

    String cardNumber = paymentRequest.getCardNumber();
    if (cardNumber.length() < 14 || cardNumber.length() > 19) {
      LOG.error("Card number has invalid length");
      return false;
    }

    for (int i = 0; i < cardNumber.length(); i++) {
      char currentCharacter = cardNumber.charAt(i);
      if (!Character.isDigit(currentCharacter)) {
        LOG.error("Card number contains non-digit character");
        return false;
      }
    }

    String cvv = paymentRequest.getCvv();
    if (cvv.length() < 3 || cvv.length() > 4) {
      LOG.error("CVV has invalid length");
      return false;
    }

    for (int i = 0; i < cvv.length(); i++) {
      char currentCharacter = cvv.charAt(i);
      if (!Character.isDigit(currentCharacter)) {
        LOG.error("CVV contains non-digit character");
        return false;
      }
    }




    return true;
  }
}
