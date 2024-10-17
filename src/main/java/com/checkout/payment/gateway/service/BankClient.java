package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.model.BankPaymentRequest;
import com.checkout.payment.gateway.model.BankPaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankClient {

  private static final String BANK_URL = "http://localhost:8080/payments";
  private final RestTemplate restTemplate;

  public BankClient() {
    this.restTemplate = new RestTemplate();
  }

  BankPaymentResponse processPayment(BankPaymentRequest postPaymentRequest) {
    return restTemplate.postForObject(BANK_URL, postPaymentRequest, BankPaymentResponse.class);
  }
}
