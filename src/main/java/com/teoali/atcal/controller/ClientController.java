package com.teoali.atcal.controller;

import com.teoali.atcal.config.MyUserPrincipal;
import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.Payment;
import com.teoali.atcal.domain.User;
import com.teoali.atcal.domain.enums.Status;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.GroupRepository;
import com.teoali.atcal.repository.PaymentRepository;
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
@RequestMapping("/clients")
public class ClientController {

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @GetMapping
  public String list(Model model, Authentication authentication) {
    model.addAttribute("clients", clientRepository.findByUser(getUser(authentication)));
    return "clients/list";
  }

  @GetMapping("/create")
  public String createForm(Model model, Authentication authentication) {
    model.addAttribute("client", new Client());
    model.addAttribute("groups", groupRepository.findByUser(getUser(authentication)));
    return "clients/create";
  }

  @PostMapping("/create")
  public String create(@ModelAttribute Client client, Authentication authentication) {
    client.setUser(getUser(authentication));
    Client savedClient = clientRepository.save(client);

    for (int i = 0; i < client.getPaymentMultiplier(); i++) {
      Payment payment = new Payment();
      payment.setClient(savedClient);
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

    return "redirect:/clients";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    if (!client.getUser().getId().equals(getUser(authentication).getId())) {
      return "home/notFound";
    }

    model.addAttribute("client", client);
    model.addAttribute("groups", groupRepository.findByUser(getUser(authentication)));
    return "clients/edit";
  }

  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute Client client, Authentication authentication) {
    client.setUser(getUser(authentication));
    client.setId(id);
    clientRepository.save(client);
    return "redirect:/clients";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id, Authentication authentication) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    if (!client.getUser().getId().equals(getUser(authentication).getId())) {
      return "home/notFound";
    }

    List<Payment> paymentList = paymentRepository.findByClient(client);

    for (Payment payment : paymentList) {
      paymentRepository.delete(payment);
    }
    
    clientRepository.deleteById(id);
    return "redirect:/clients";
  }

  private User getUser(Authentication authentication) {
    return ((MyUserPrincipal) authentication.getPrincipal()).getUser();
  }
}
