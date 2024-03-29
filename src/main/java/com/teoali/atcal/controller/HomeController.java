package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Home;
import com.teoali.atcal.domain.User;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.PaymentRepository;
import com.teoali.atcal.repository.UserRepository;
import com.teoali.atcal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

  SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Autowired
  private UserService userService;

  @GetMapping
  public String home() {
    return "home/home";
  }

  @GetMapping("/index")
  public String index(Model model, Authentication authentication) {
    User user = userRepository.findById(userService.getUser(authentication).getId()).orElseThrow(() -> new IllegalArgumentException("Invalid user"));

    Home home = new Home();
    home.setQuantityClients(clientRepository.findByUser(user).size());
    home.setQuantityClientsWithDebts(paymentRepository.getDebtPayments(user.getId()).size());
    home.setSumAmountToReceiveActualMonth(paymentRepository.getSumAmountToReceive(user.getId(), firstDayOfMonth(), lastDayOfMonth()));
    home.setSumAmountToReceiveNextMonth(paymentRepository.getSumAmountToReceive(user.getId(), firstDayOfNextMonth(), lastDayOfNextMonth()));

    model.addAttribute("user", user);
    model.addAttribute("home", home);
    return "home/index";
  }

  @PostMapping("/register")
  public String register(@ModelAttribute User user, Model model) {
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    user.setCreatedAt(LocalDateTime.now());
    userRepository.save(user);

    model.addAttribute("user", user);
    model.addAttribute("successRegister", true);
    return "home/login";
  }

  @GetMapping("/login")
  public String login() {
    return "home/login";
  }

  @GetMapping("/register")
  public String register() {
    return "home/register";
  }

  @GetMapping("/logout")
  public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
    this.logoutHandler.logout(request, response, authentication);
    return "redirect:/clients";
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
    LocalDate nextMonth = LocalDate.now().plusMonths(1);
    return nextMonth.withDayOfMonth(nextMonth.lengthOfMonth());
  }
  // TODO UNIFY THIS METHOD ON ALL CONTROLLERS

}
