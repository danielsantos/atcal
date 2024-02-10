package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Client;
import com.teoali.atcal.repository.ClientRepository;
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

  @GetMapping("/list/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    model.addAttribute("client", client);
    return "payments/listPayments";
  }
}
