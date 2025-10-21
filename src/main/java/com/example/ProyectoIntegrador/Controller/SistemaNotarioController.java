package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Notario;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Entity.Servicio;
import com.example.ProyectoIntegrador.Repository.NotarioRepository;
import com.example.ProyectoIntegrador.Repository.UsuarioRepository;
import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class SistemaNotarioController {

    @Autowired
    private NotarioRepository notarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @GetMapping("/SistemaNotario")
    public String sistemaNotario(Authentication authentication, Model model) {
        String nombreUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario).orElse(null);

        if (usuario != null && usuario.getRol() == Usuario.Rol.notario) {
            Notario notario = notarioRepository.findByUsuario(usuario).orElse(null);
            if (notario != null) {
                model.addAttribute("nombreNotario", notario.getNombre() + " " + notario.getApellido());
            } else {
                model.addAttribute("nombreNotario", usuario.getNombreUsuario());
            }
        }

        List<Servicio> servicios = servicioRepository.findAll();
        model.addAttribute("servicios", servicios);
        model.addAttribute("servicio", new Servicio());

        return "SistemaNotario";
    }
}
