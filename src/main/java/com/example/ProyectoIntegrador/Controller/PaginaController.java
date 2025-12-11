package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Tramite;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PaginaController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private TramiteRepository tramiteRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private PagoRepository pagoRepository;

    @GetMapping("/Pagina")
    public String inicio(Model model, Authentication authentication) {
        model.addAttribute("servicios", servicioRepository.findAll());

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElse(null);

            if (usuario != null) {
                Cliente cliente = clienteRepository.findAll().stream()
                        .filter(c -> c.getUsuario() != null &&
                                c.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                        .findFirst().orElse(null);

                if(cliente != null) {
                    List<Tramite> tramites = tramiteRepository.findByClienteOrderByFechaInicioDesc(cliente);
                    model.addAttribute("misTramites", tramites);

                    Map<Integer, Boolean> estadoPagos = new HashMap<>();
                    for (Tramite t : tramites) {
                        estadoPagos.put(t.getIdTramite(), pagoRepository.existsByTramite(t));
                    }
                    model.addAttribute("estadoPagos", estadoPagos);
                }
            }
        }
        return "Pagina";
    }
}