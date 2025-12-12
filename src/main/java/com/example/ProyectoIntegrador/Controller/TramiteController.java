package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Servicio;
import com.example.ProyectoIntegrador.Entity.Tramite;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Entity.HistorialTramite;
import com.example.ProyectoIntegrador.Repository.ClienteRepository;
import com.example.ProyectoIntegrador.Repository.ServicioRepository;
import com.example.ProyectoIntegrador.Repository.TramiteRepository;
import com.example.ProyectoIntegrador.Repository.UsuarioRepository;
import com.example.ProyectoIntegrador.Repository.HistorialTramiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;


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
    @Autowired
    private HistorialTramiteRepository historialTramiteRepository;

    
    @PostMapping("/tramite/solicitar")
    public String solicitarTramite(@RequestParam("idServicio") Integer idServicio,
                                   @RequestParam("descripcion") String descripcion,
                                   Authentication authentication) {

        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElse(null);
        Cliente cliente = clienteRepository.findAll().stream()
                .filter(c -> c.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .findFirst().orElse(null);

        Servicio servicio = servicioRepository.findById(idServicio).orElse(null);

        if (cliente != null && servicio != null) {
            Tramite tramite = new Tramite();
            tramite.setCliente(cliente);
            tramite.setServicio(servicio);
            tramite.setObservaciones(descripcion);
            tramite.setEstado(Tramite.Estado.pendiente);
            Tramite guardado = tramiteRepository.save(tramite);

            // Guardar Historial Inicial
            crearHistorial(guardado, Tramite.Estado.pendiente, "Solicitud creada por el cliente.");
        }
        return "redirect:/Pagina?solicitud=exito";
    }


    @GetMapping("/tramite/cambiarEstado/{id}/{estado}")
    public String cambiarEstado(@PathVariable Integer id, @PathVariable String estado) {
        Tramite tramite = tramiteRepository.findById(id).orElse(null);
        if (tramite != null) {
            try {
                Tramite.Estado nuevoEstado = Tramite.Estado.valueOf(estado);
                tramite.setEstado(nuevoEstado);
                tramiteRepository.save(tramite);

                // Guardar en Historial automáticamente
                crearHistorial(tramite, nuevoEstado, "El notario cambió el estado a " + estado.toUpperCase());

            } catch (IllegalArgumentException e) {
                System.out.println("Estado no válido");
            }
        }
        return "redirect:/SistemaNotario?seccion=solicitudes";
    }

    // Método auxiliar para guardar historial
    private void crearHistorial(Tramite tramite, Tramite.Estado estado, String comentario) {
        HistorialTramite historial = new HistorialTramite();
        historial.setTramite(tramite);
        historial.setEstado(estado);
        historial.setFecha(new Date());
        historial.setComentario(comentario);
        historialTramiteRepository.save(historial);
    }
}