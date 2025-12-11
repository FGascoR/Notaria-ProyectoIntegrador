package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Notario;
import com.example.ProyectoIntegrador.Entity.Servicio;
import com.example.ProyectoIntegrador.Entity.Tramite; // <--- Importante
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SistemaNotarioController {

    @Autowired
    private NotarioRepository notarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    
    @Autowired
    private TramiteRepository tramiteRepository;
   

    @GetMapping("/SistemaNotario")
    public String sistemaNotario(Authentication authentication, Model model,
                                 @RequestParam(required = false) String seccion) {

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

        
        List<Tramite> pendientes = tramiteRepository.findByEstado(Tramite.Estado.pendiente);
        model.addAttribute("solicitudesPendientes", pendientes);


        List<Tramite> aceptados = tramiteRepository.findByEstado(Tramite.Estado.aceptado);
        model.addAttribute("tramitesEnProceso", aceptados);


        Map<Integer, Boolean> estadoPagos = new HashMap<>();
        for (Tramite t : aceptados) {
            estadoPagos.put(t.getIdTramite(), pagoRepository.existsByTramite(t));
        }
        model.addAttribute("estadoPagos", estadoPagos);

        model.addAttribute("servicios", servicioRepository.findAll());
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("seccion", seccion != null ? seccion : "inicio");

        return "SistemaNotario";
    }
}