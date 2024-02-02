package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Group;
import com.teoali.atcal.repository.GroupRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  @GetMapping
  public String list(Model model) {
    List<Group> groups = groupRepository.findAll();
    model.addAttribute("groups", groups);
    return "groups/list";
  }

  @GetMapping("/create")
  public String createForm(Model model) {
    model.addAttribute("group", new Group());
    return "groups/create";
  }

  @PostMapping("/create")
  public String create(@ModelAttribute Group group) {
    groupRepository.save(group);
    return "redirect:/groups";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Group group = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid group Id: " + id));
    model.addAttribute("group", group);
    return "groups/edit";
  }

  @PostMapping("/edit/{id}")
  public String edit(@PathVariable Long id, @ModelAttribute Group group) {
    group.setId(id);
    groupRepository.save(group);
    return "redirect:/groups";
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Long id) {
    groupRepository.deleteById(id);
    return "redirect:/groups";
  }
}
