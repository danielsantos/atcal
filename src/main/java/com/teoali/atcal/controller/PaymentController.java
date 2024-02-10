package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Payment;
import com.teoali.atcal.domain.enums.Status;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.PaymentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @GetMapping("/list/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    model.addAttribute("client", client);
    model.addAttribute("payments", paymentRepository.findByClient(client));

    return "payments/listPayments";
  }

  @GetMapping("/paid_out/{id}")
  public String paidOut(@PathVariable Long id) {
    Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
    payment.setStatus(Status.PAGO.getId());
    paymentRepository.save(payment);
    return "redirect:/payments/list/" + payment.getClient().getId();
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
    paymentRepository.deleteById(id);
    return "redirect:/payments/list/" + payment.getClient().getId();
  }
}
