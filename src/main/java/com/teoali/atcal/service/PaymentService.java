package com.teoali.atcal.service;

import com.teoali.atcal.domain.Payment;
import com.teoali.atcal.domain.enums.Status;
import com.teoali.atcal.repository.PaymentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Autowired
  private PaymentRepository paymentRepository;

  public String updatePastDuePayments() {
    List<Payment> payments = paymentRepository.getPaymentsPastDue(LocalDate.now());

    AtomicInteger count = new AtomicInteger();

    payments.forEach(it -> {
      it.setStatus(Status.VENCIDO.getId());
      paymentRepository.save(it);
      count.getAndIncrement();
    });

    return "paymentsGet: " + payments.size() + " - Updated " + count + " past due payments";
  }
}
