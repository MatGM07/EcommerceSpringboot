package com.comercio.comercio.controller;

import com.comercio.comercio.model.Usuario;
import com.comercio.comercio.service.IUsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.swing.text.html.Option;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;


@Controller
@RequestMapping("/usuario")
public class UsuarioController{

    @Autowired
    private IUsuarioService usuarioService;

    BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

    @GetMapping("/registro")
    public String create(HttpSession session){
        if (session.getAttribute("idusuario") != null) {
            // Si hay una sesión iniciada
            return "redirect:/"; // Redirige al dashboard u otra página protegida
        }else{
            return  "usuario/registro";
        }
    }

    @PostMapping("/save")
    public String save(Usuario usuario, RedirectAttributes redirectAttributes){
        usuario.setTipo("USER");
        usuario.setPassword(passEncode.encode(usuario.getPassword()));
        try {
            usuarioService.save(usuario);
        }catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("registroError", true);
            // Maneja la excepción cuando el valor único ya existe en la base de datos
            return "redirect:/usuario/registro"; // Redirige a una página de error, por ejemplo
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("registroError", true);
            // Maneja cualquier otra excepción que pueda ocurrir
            return "redirect:/usuario/registro"; // Redirige a una página de error general
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(HttpSession session){
        if (session.getAttribute("idusuario") != null) {
            // Si hay una sesión iniciada
            return "redirect:/"; // Redirige al dashboard u otra página protegida
        }else{
            return  "usuario/login";
        }
    }

    @GetMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session){

        Optional<Usuario> user=usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
        //logger.info("Usuario de db: {}", user.get());

        if (user.isPresent()) {
            session.setAttribute("idusuario", user.get().getId());

            if (user.get().getTipo().equals("ADMIN")) {
                return "redirect:/administrador";
            }else {
                return "redirect:/";
            }
        }else {
            return "redirect:/usuario/login";
        }

    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}