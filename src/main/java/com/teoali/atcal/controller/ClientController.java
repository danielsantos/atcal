package com.teoali.atcal.controller;

import com.teoali.atcal.config.MyUserPrincipal;
import com.teoali.atcal.domain.Client;
import com.teoali.atcal.domain.User;
import com.teoali.atcal.repository.ClientRepository;
import com.teoali.atcal.repository.GroupRepository;
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
    clientRepository.save(client);
    return "redirect:/clients";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
    Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid client Id: " + id));
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
  public String delete(@PathVariable Long id) {
    clientRepository.deleteById(id);
    return "redirect:/clients";
  }

  private User getUser(Authentication authentication) {
    return ((MyUserPrincipal) authentication.getPrincipal()).getUser();
  }
}
