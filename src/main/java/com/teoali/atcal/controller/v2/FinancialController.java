package com.teoali.atcal.controller.v2;

import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.v2.Expense;
import com.teoali.atcal.domain.v2.ReferMonth;
import com.teoali.atcal.repository.v2.ExpenseRepository;
import com.teoali.atcal.repository.v2.PaymentMethodRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@RequestMapping("/financial")
public class FinancialController {

  @Autowired
  private ExpenseRepository repository;

  @Autowired
  private PaymentMethodRepository paymentMethodRepository;

  @GetMapping("/list")
  public String list(Model model) {
    List<Expense> list = repository.getByReferMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
    ReferMonth referMonth = new ReferMonth();
    referMonth.setDate(LocalDate.now().getMonthValue() +"/"+ LocalDate.now().getYear());

    BigDecimal total = list.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

    model.addAttribute("expenses", list);
    model.addAttribute("total", total);
    model.addAttribute("referMonth", referMonth);
    return "v2/expense/list";
  }

  @PostMapping("/list/month")
  public String listReferMonth(Model model, @ModelAttribute ReferMonth referMonth) {
    List<Expense> list;

    if (referMonth.getDate() == null) {
      list = repository.getByReferMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
    } else {
      String[] dates = referMonth.getDate().split("/");
      list = repository.getByReferMonth(Integer.parseInt(dates[1]), Integer.parseInt(dates[0]));
    }

    BigDecimal total = list.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

    model.addAttribute("expenses", list);
    model.addAttribute("total", total);
    model.addAttribute("referMonth", referMonth);
    return "v2/expense/list";
  }

  @GetMapping
  public String createForm(Model model) {
    model.addAttribute("paymentMethods", paymentMethodRepository.findAll());
    return "v2/expense/create";
  }

  @PostMapping("/create")
  public String create(@ModelAttribute Expense expense) {
    expense.setCreatedAt(LocalDateTime.now());
    repository.save(expense);
    return "redirect:/financial";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id, Model model) {
    Expense expense = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid expense Id:" + id));
    repository.delete(expense);

    String referMonth = expense.getDate().getMonthValue() + "/" + expense.getDate().getYear();

    model.addAttribute("expenses", repository.getByReferMonth(LocalDate.now().getYear(), LocalDate.now().getMonthValue()));
    model.addAttribute("referMonth", new ReferMonth(referMonth));
    return "v2/expense/list";
  }
}
