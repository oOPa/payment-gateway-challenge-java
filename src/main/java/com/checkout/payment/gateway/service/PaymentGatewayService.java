package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.BankPaymentRequest;
import com.checkout.payment.gateway.model.BankPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;
  private final BankClient bankClient;
  private final PaymentGatewayInputConverter paymentGatewayInputConverter;
  private final PaymentGatewayValidator paymentGatewayValidator;

  public PaymentGatewayService(PaymentsRepository paymentsRepository, BankClient bankClient) {
    this.paymentsRepository = paymentsRepository;
    this.bankClient = bankClient;
    this.paymentGatewayInputConverter = new PaymentGatewayInputConverter();
    this.paymentGatewayValidator = new PaymentGatewayValidator();
  }

  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    final UUID uuid = UUID.randomUUID();

    final PostPaymentResponse postPaymentResponse;
    if (paymentGatewayValidator.isRequestValid(paymentRequest)) {
      BankPaymentRequest bankPaymentRequest = new BankPaymentRequest();
      bankPaymentRequest.setAmount(paymentRequest.getAmount());
      bankPaymentRequest.setCardNumber(paymentRequest.getCardNumber());
      bankPaymentRequest.setCurrency(paymentRequest.getCurrency());
      bankPaymentRequest.setExpiryDate(paymentRequest.getExpiryDate());
      bankPaymentRequest.setCvv(Integer.parseInt(paymentRequest.getCvv()));

      BankPaymentResponse bankPaymentResponse = bankClient.processPayment(bankPaymentRequest);
      PaymentStatus paymentStatus = bankPaymentResponse.getAuthorized() ? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED;
      postPaymentResponse = paymentGatewayInputConverter.generateValidPostPaymentResponse(paymentRequest, paymentStatus, uuid);
    } else {
      postPaymentResponse = paymentGatewayInputConverter.generateRejectedPaymentResponse(uuid);
    }
    paymentsRepository.add(postPaymentResponse);
    return postPaymentResponse;
  }

}
