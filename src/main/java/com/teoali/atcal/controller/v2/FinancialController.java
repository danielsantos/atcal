package com.teoali.atcal.controller.v2;

import ch.qos.logback.classic.Logger;
import com.teoali.atcal.domain.Client;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.GroupRepository;
import com.teoali.atcal.repository.PaymentRepository;
import com.teoali.atcal.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/financial")
public class FinancialV2Controller {

  Logger logger = (Logger) LoggerFactory.getLogger(FinancialV2Controller.class);

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private UserService userService;

  @GetMapping
  public String createForm(Model model, Authentication authentication) {
    model.addAttribute("client", new Client());
    model.addAttribute("groups", groupRepository.findByUser(userService.getUser(authentication)));
    return "clients/create";
  }
}
