package com.example.ProyectoIntegrador.Controller;

import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/Login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_NOTARIO"))) {
                return "redirect:/SistemaNotario";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
                return "redirect:/Pagina";
            }
        }
        return "Login";
    }

    // IMPORTANTE: Aquí NO debe haber ningún método para "/Pagina"
}