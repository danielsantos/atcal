package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Group;
import com.teoali.atcal.repository.GroupRepository;
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
@RequestMapping("/groups")
public class GroupController {

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private UserService userService;

  @GetMapping
  public String list(Model model, Authentication authentication) {
    List<Group> groups = groupRepository.findByUser(userService.getUser(authentication));
    model.addAttribute("groups", groups);
    return "groups/list";
  }

  @GetMapping("/create")
  public String createForm(Model model) {
    model.addAttribute("group", new Group());
    return "groups/create";
  }

  @PostMapping("/create")
  public String create(@ModelAttribute Group group, Authentication authentication) {
    group.setUser(userService.getUser(authentication));
    group.setCreatedAt(LocalDateTime.now());
    groupRepository.save(group);
    return "redirect:/groups";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model, Authentication authentication) {
    Group group = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid group Id: " + id));
    if (!group.getUser().getId().equals(userService.getUser(authentication).getId())) {
      return "home/notFound";
    }

    model.addAttribute("group", group);
    return "groups/edit";
  }

  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute Group group, Authentication authentication) {
    Group groupDB = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid group Id: " + id));
    group.setUpdatedAt(LocalDateTime.now());
    group.setCreatedAt(groupDB.getCreatedAt());
    group.setId(id);
    group.setUser(userService.getUser(authentication));
    groupRepository.save(group);
    return "redirect:/groups";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id, Authentication authentication) {
    Group group = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid group Id: " + id));
    if (!group.getUser().getId().equals(userService.getUser(authentication).getId())) {
      return "home/notFound";
    }

    groupRepository.deleteById(id);
    return "redirect:/groups";
  }
}
