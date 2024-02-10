package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Payment;
import com.teoali.atcal.domain.enums.Status;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.PaymentRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @GetMapping("/create/{id}")
  public String createForm(@PathVariable Long id, Model model) {
    model.addAttribute("client", clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id)));
    model.addAttribute("payment", new Payment());
    return "payments/createPayments";
  }

  @PostMapping("/create/{id}")
  public String create(@PathVariable Long id, @ModelAttribute Client client) { // TODO RECEBER O ID DO CLIENTE
    for (int i = 0; i < client.getPaymentMultiplier(); i++) {
      Payment payment = new Payment();
      payment.setClient(client);
      payment.setAmount(client.getAmount());
      payment.setStatus(Status.EM_ABERTO.getId());

      LocalDate currentDate = LocalDate.now();
      LocalDate dueDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), client.getDueDay());
      if (dueDate.isBefore(currentDate) || dueDate.isEqual(currentDate)) {
        dueDate = dueDate.plusMonths(1);
      }

      if (i != 0) {
        dueDate = dueDate.plusMonths(i);
      }

      payment.setDueDate(dueDate);
      paymentRepository.save(payment);
    }

    return "redirect:/payments/list/" + client.getId();
  }

  @GetMapping("/list/{id}")
  public String list(@PathVariable Long id, Model model) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    model.addAttribute("client", client);
    model.addAttribute("payments", paymentRepository.findByClient(client));

    return "payments/listPayments";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
    Client client = clientRepository.findById(payment.getClient().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid client Id:" + payment.getClient().getId()));
    model.addAttribute("client", client);
    model.addAttribute("payment", payment);
    return "payments/editPayment";
  }

  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute Payment payment) {
    Payment paymentToGetClient = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));

    payment.setId(id);
    payment.setClient(paymentToGetClient.getClient());
    paymentRepository.save(payment);
    return "redirect:/payments/list/" + paymentToGetClient.getClient().getId();
  }

  @GetMapping("/paid_out/{id}")
  public String paidOut(@PathVariable Long id) {
    Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
    payment.setStatus(Status.PAGO.getId());
    payment.setPayDate(LocalDate.now());
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
