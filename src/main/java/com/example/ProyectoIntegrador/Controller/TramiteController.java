package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Servicio;
import com.example.ProyectoIntegrador.Entity.Tramite;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.ClienteRepository;
import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import com.example.ProyectoIntegrador.Repository.TramiteRepository;
import com.example.ProyectoIntegrador.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TramiteController {

    @Autowired
    private TramiteRepository tramiteRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    // 1. GUARDAR LA SOLICITUD DEL CLIENTE (Esta es la parte que te estaba fallando)
    @PostMapping("/tramite/solicitar")
    public String solicitarTramite(@RequestParam("idServicio") Integer idServicio,
                                   @RequestParam("descripcion") String descripcion,
                                   Authentication authentication) {

        if (authentication == null) {
            return "redirect:/Login";
        }

        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElse(null);

        // Buscamos al cliente asociado a este usuario
        Cliente cliente = clienteRepository.findAll().stream()
                .filter(c -> c.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .findFirst().orElse(null);

        Servicio servicio = servicioRepository.findById(idServicio).orElse(null);

        if (cliente != null && servicio != null) {
            Tramite tramite = new Tramite();
            tramite.setCliente(cliente);
            tramite.setServicio(servicio);
            tramite.setObservaciones(descripcion);

            // Estado inicial: pendiente
            tramite.setEstado(Tramite.Estado.pendiente);

            tramiteRepository.save(tramite);
        }
        return "redirect:/Pagina?solicitud=exito";
    }

    // 2. CAMBIAR ESTADO (Usado por el Notario en su bandeja)
    @GetMapping("/tramite/cambiarEstado/{id}/{estado}")
    public String cambiarEstado(@PathVariable Integer id, @PathVariable String estado) {
        Tramite tramite = tramiteRepository.findById(id).orElse(null);
        if (tramite != null) {
            try {
                // Convertimos el string (ej: "aceptado") al Enum correspondiente
                tramite.setEstado(Tramite.Estado.valueOf(estado));
                tramiteRepository.save(tramite);
            } catch (IllegalArgumentException e) {
                System.out.println("Estado no válido: " + estado);
            }
        }
        return "redirect:/SistemaNotario?seccion=solicitudes";
    }
}