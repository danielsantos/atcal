package com.teoali.atcal.controller;

import com.teoali.atcal.config.MyUserPrincipal;
import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Payment;
import com.teoali.atcal.domain.User;
import com.teoali.atcal.domain.enums.Status;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.PaymentRepository;
import com.teoali.atcal.service.UserService;
import java.time.LocalDate;
import java.util.List;
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

  @Autowired
  private UserService userService;

  @GetMapping("/create/{id}")
  public String createForm(@PathVariable Long id, Model model, Authentication authentication) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    if (!client.getUser().getId().equals(userService.getUser(authentication).getId())) {
      return "home/notFound";
    }

    model.addAttribute("client", client);
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
  public String list(@PathVariable Long id, Model model, Authentication authentication) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    if (!client.getUser().getId().equals(userService.getUser(authentication).getId())) {
      return "home/notFound";
    }

    model.addAttribute("client", client);
    model.addAttribute("payments", paymentRepository.findByClient(client));

    return "payments/listPayments";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
    Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
    if (!payment.getClient().getUser().getId().equals(userService.getUser(authentication).getId())) {
      return "home/notFound";
    }

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
  public String paidOut(@PathVariable Long id, Authentication authentication) {
    Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
    if (!payment.getClient().getUser().getId().equals(userService.getUser(authentication).getId())) {
      return "home/notFound";
    }

    payment.setStatus(Status.PAGO.getId());
    payment.setPayDate(LocalDate.now());
    paymentRepository.save(payment);
    return "redirect:/payments/list/" + payment.getClient().getId();
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id, Authentication authentication) {
    Payment payment = paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment Id:" + id));
    if (!payment.getClient().getUser().getId().equals(userService.getUser(authentication).getId())) {
      return "home/notFound";
    }

    paymentRepository.deleteById(id);
    return "redirect:/payments/list/" + payment.getClient().getId();
  }

  @GetMapping("/list/clients/debt")
  public String listClientsWithDebt(Model model, Authentication authentication) {
    model.addAttribute("payments", paymentRepository.getDebtPayments(userService.getUser(authentication).getId()));
    return "payments/listClientsWithDebt";
  }

  @GetMapping("/list/receive/actual/month")
  public String listPaymentsToReceiveActualMonth(Model model, Authentication authentication) {
    model.addAttribute("payments", paymentRepository.getPaymentsToReceive(userService.getUser(authentication).getId(), firstDayOfMonth(), lastDayOfMonth()));
    model.addAttribute("totalAmount", paymentRepository.getSumAmountToReceive(userService.getUser(authentication).getId(), firstDayOfMonth(), lastDayOfMonth()));
    model.addAttribute("firstDayMonth", firstDayOfMonth());
    return "payments/listPaymentsToReceive";
  }

  @GetMapping("/list/receive/next/month")
  public String listPaymentsToReceiveNextMonth(Model model, Authentication authentication) {
    model.addAttribute("payments", paymentRepository.getPaymentsToReceive(userService.getUser(authentication).getId(), firstDayOfNextMonth(), lastDayOfNextMonth()));
    model.addAttribute("totalAmount", paymentRepository.getSumAmountToReceive(userService.getUser(authentication).getId(), firstDayOfNextMonth(), lastDayOfNextMonth()));
    model.addAttribute("firstDayMonth", firstDayOfNextMonth());
    return "payments/listPaymentsToReceive";
  }

  @PostMapping("/update/past/due/payments")
  public void updatePastDuePayments() {
    List<Payment> payments = paymentRepository.getPaymentsPastDue(LocalDate.now());

    payments.forEach(it -> {
      it.setStatus(Status.VENCIDO.getId());
      paymentRepository.save(it);
    });
  }

  // TODO UNIFY THIS METHOD ON ALL CONTROLLERS
  private LocalDate firstDayOfMonth() {
    return LocalDate.now().withDayOfMonth(1);
  }

  private LocalDate lastDayOfMonth() {
    return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
  }

  private LocalDate firstDayOfNextMonth() {
    return LocalDate.now().plusMonths(1).withDayOfMonth(1);
  }

  private LocalDate lastDayOfNextMonth() {
      return LocalDate.now().plusMonths(1).withDayOfMonth(LocalDate.now().lengthOfMonth());
  }
  // TODO UNIFY THIS METHOD ON ALL CONTROLLERS

}
