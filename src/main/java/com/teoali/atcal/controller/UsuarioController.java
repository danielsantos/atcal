package com.teoali.atcal.controller;

import com.teoali.atcal.domain.Usuario;
import com.teoali.atcal.repository.UsuarioRepository;
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
public class UsuarioController {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @GetMapping
  public String listarUsuarios(Model model) {
    List<Usuario> usuarios = usuarioRepository.findAll();
    model.addAttribute("usuarios", usuarios);
    return "listar";
  }

  @GetMapping("/criar")
  public String exibirFormularioCriar(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "criar";
  }

  @PostMapping("/criar")
  public String criarUsuario(@ModelAttribute Usuario usuario) {
    usuarioRepository.save(usuario);
    return "redirect:/";
  }

  @GetMapping("/editar/{id}")
  public String exibirFormularioEditar(@PathVariable Long id, Model model) {
    Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID de usuário inválido"));
    model.addAttribute("usuario", usuario);
    return "editar";
  }

  @PostMapping("/editar/{id}")
  public String editarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario) {
    usuario.setId(id);
    usuarioRepository.save(usuario);
    return "redirect:/";
  }

  @GetMapping("/excluir/{id}")
  public String excluirUsuario(@PathVariable Long id) {
    usuarioRepository.deleteById(id);
    return "redirect:/";
  }
}
