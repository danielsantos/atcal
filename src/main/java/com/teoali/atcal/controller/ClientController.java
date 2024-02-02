package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Client;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  @GetMapping
  public String list(Model model) {
    model.addAttribute("clients", clientRepository.findAll());
    return "clients/list";
  }

  @GetMapping("/create")
  public String createForm(Model model) {
    model.addAttribute("client", new Client());
    model.addAttribute("groups", groupRepository.findAll());
    return "clients/create";
  }

  @PostMapping("/create")
  public String create(@ModelAttribute Client client) {
    clientRepository.save(client);
    return "redirect:/clients";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
    model.addAttribute("client", client);
    return "clients/edit";
  }

  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute Client client) {
    client.setId(id);
    clientRepository.save(client);
    return "redirect:/clients";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    clientRepository.deleteById(id);
    return "redirect:/clients";
  }
}
