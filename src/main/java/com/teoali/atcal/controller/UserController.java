package com.teoali.atcal.controller;

import com.teoali.atcal.config.MyUserPrincipal;
import com.teoali.atcal.domain.User;
import com.teoali.atcal.repository.UserRepository;
import com.teoali.atcal.service.UserService;
import java.time.LocalDateTime;
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
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @GetMapping
  public String list(Model model, Authentication authentication) {
    if (userService.getUser(authentication).getAdministrator() == null || userService.getUser(authentication).getAdministrator() != 1) {
      return "home/notFound";
    }

    List<User> users = userRepository.findAll();
    model.addAttribute("users", users);
    return "users/list";
  }

  @GetMapping("/create")
  public String createForm(Model model, Authentication authentication) {
    if (userService.getUser(authentication).getAdministrator() == null || userService.getUser(authentication).getAdministrator() != 1) {
      return "home/notFound";
    }

    model.addAttribute("user", new User());
    return "users/create";
  }

  @PostMapping("/create")
  public String criarUsuario(@ModelAttribute User user, Authentication authentication) {
    if (userService.getUser(authentication).getAdministrator() == null || userService.getUser(authentication).getAdministrator() != 1) {
      return "home/notFound";
    }

    user.setCreatedAt(LocalDateTime.now());
    userRepository.save(user);
    return "redirect:/users";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
    if (userService.getUser(authentication).getAdministrator() == null || userService.getUser(authentication).getAdministrator() != 1) {
      return "home/notFound";
    }

    User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
    model.addAttribute("user", user);
    return "users/edit";
  }

  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute User user, Authentication authentication) {
    if (userService.getUser(authentication).getAdministrator() == null || userService.getUser(authentication).getAdministrator() != 1) {
      return "home/notFound";
    }
    User userDB = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));

    user.setUpdatedAt(LocalDateTime.now());
    user.setCreatedAt(userDB.getCreatedAt());
    user.setId(id);
    userRepository.save(user);
    return "redirect:/users";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id, Authentication authentication) {
    if (userService.getUser(authentication).getAdministrator() == null || userService.getUser(authentication).getAdministrator() != 1) {
      return "home/notFound";
    }

    userRepository.deleteById(id);
    return "redirect:/users";
  }
}
