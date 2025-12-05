package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Tramite;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.ClienteRepository;
import com.example.ProyectoIntegrador.Repository.PagoRepository;
import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import com.example.ProyectoIntegrador.Repository.TramiteRepository;
import com.example.ProyectoIntegrador.Repository.UsuarioRepository;
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
    @Autowired private PagoRepository pagoRepository; // Inyección nueva

    @GetMapping("/Pagina")
    public String inicio(Model model, Authentication authentication) {
        model.addAttribute("servicios", servicioRepository.findAll());

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElse(null);

            if (usuario != null) {
                Cliente cliente = clienteRepository.findByUsuario(usuario).orElse(null);

                if (cliente != null) {
                    List<Tramite> tramites = tramiteRepository.findByClienteOrderByFechaInicioDesc(cliente);
                    model.addAttribute("misTramites", tramites);

                    // --- NUEVA LÓGICA: Mapa de Pagos ---
                    Map<Integer, Boolean> estadoPagos = new HashMap<>();
                    for (Tramite t : tramites) {
                        estadoPagos.put(t.getIdTramite(), pagoRepository.existsByTramite(t));
                    }
                    model.addAttribute("estadoPagos", estadoPagos);
                    // -----------------------------------
                }
            }
        }
        return "Pagina";
    }
}