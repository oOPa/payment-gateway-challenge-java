package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentGatewayValidatorTest {

  @Test
  void isRequestValidReturnsTrueForValidRequests() {
    PaymentGatewayValidator paymentGatewayValidator = new PaymentGatewayValidator();
    PostPaymentRequest paymentRequest = generateValidPostPaymentRequest();
    boolean requestValid = paymentGatewayValidator.isRequestValid(paymentRequest);

    assertTrue(requestValid);
  }

  @Test
  void isRequestValidReturnsFalseForInvalidRequests() {
    PaymentGatewayValidator paymentGatewayValidator = new PaymentGatewayValidator();
    PostPaymentRequest postPaymentRequest = generateValidPostPaymentRequest();

    // Invalid amount
    postPaymentRequest.setAmount(-1);
    assertFalse(paymentGatewayValidator.isRequestValid(postPaymentRequest), "Check if amount is invalid");

    // Invalid month
    postPaymentRequest = generateValidPostPaymentRequest();
    postPaymentRequest.setExpiryMonth(0);
    assertFalse(paymentGatewayValidator.isRequestValid(postPaymentRequest), "Check if expiry month is invalid");

    // Invalid expiry year
    postPaymentRequest = generateValidPostPaymentRequest();
    postPaymentRequest.setExpiryYear(1900);
    assertFalse(paymentGatewayValidator.isRequestValid(postPaymentRequest), "Check if expiry year is invalid (less than current year)");

    // Unsupported currency
    postPaymentRequest = generateValidPostPaymentRequest();
    postPaymentRequest.setCurrency("XYZ");
    assertFalse(paymentGatewayValidator.isRequestValid(postPaymentRequest), "Check if currency is unsupported");

    // Invalid card length
    postPaymentRequest = generateValidPostPaymentRequest();
    postPaymentRequest.setCardNumber("123456789");
    assertFalse(paymentGatewayValidator.isRequestValid(postPaymentRequest), "Check if card number is too short");

    // Invalid card number
    postPaymentRequest = generateValidPostPaymentRequest();
    postPaymentRequest.setCardNumber("1234abcd5678");
    assertFalse(paymentGatewayValidator.isRequestValid(postPaymentRequest), "Check if card number contains non-digit characters");

    // Invalid CVV
    postPaymentRequest = generateValidPostPaymentRequest();
    postPaymentRequest.setCvv("121111");
    assertFalse(paymentGatewayValidator.isRequestValid(postPaymentRequest), "Check if CVV is too long");
  }

  private static PostPaymentRequest generateValidPostPaymentRequest() {
    PostPaymentRequest postPaymentRequest = new PostPaymentRequest();
    postPaymentRequest.setCardNumber("876543210987654321");
    postPaymentRequest.setExpiryMonth(2);
    postPaymentRequest.setExpiryYear(2030);
    postPaymentRequest.setCurrency("GBP");
    postPaymentRequest.setAmount(341);
    postPaymentRequest.setCvv("945");

    return postPaymentRequest;
  }
}