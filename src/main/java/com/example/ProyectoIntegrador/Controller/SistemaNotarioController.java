package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Notario;
import com.example.ProyectoIntegrador.Entity.Servicio;
import com.example.ProyectoIntegrador.Entity.Tramite;
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
    private ClienteRepository clienteRepository;

    @Autowired
    private TramiteRepository tramiteRepository; // Inyectamos el repositorio de trámites

    @Autowired
    private PagoRepository pagoRepository; // Inyectamos para verificar pagos

    @GetMapping("/SistemaNotario")
    public String sistemaNotario(Authentication authentication, Model model,
                                 @RequestParam(required = false) String seccion) {

        String nombreUsuario = authentication.getName();
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario).orElse(null);

        // Lógica para mostrar el nombre del notario en el saludo
        if (usuario != null && usuario.getRol() == Usuario.Rol.notario) {
            Notario notario = notarioRepository.findByUsuario(usuario).orElse(null);
            if (notario != null) {
                model.addAttribute("nombreNotario", notario.getNombre() + " " + notario.getApellido());
            } else {
                model.addAttribute("nombreNotario", usuario.getNombreUsuario());
            }
        }

        // --- LÓGICA DE BANDEJAS ---

        // 1. BANDEJA DE SOLICITUDES (Solo Pendientes)
        List<Tramite> pendientes = tramiteRepository.findByEstado(Tramite.Estado.pendiente);
        model.addAttribute("solicitudesPendientes", pendientes);

        // 2. TRAMITES EN PROCESO (Los que el notario ya aceptó)
        List<Tramite> aceptados = tramiteRepository.findByEstado(Tramite.Estado.aceptado);
        model.addAttribute("tramitesEnProceso", aceptados);

        // 3. MAPA DE ESTADO DE PAGOS (Para saber qué trámites aceptados ya fueron pagados)
        Map<Integer, Boolean> estadoPagos = new HashMap<>();
        for (Tramite t : aceptados) {
            // Verifica si existe un pago registrado para ese trámite
            estadoPagos.put(t.getIdTramite(), pagoRepository.existsByTramite(t));
        }
        model.addAttribute("estadoPagos", estadoPagos);

        // --- DATOS GENERALES ---
        model.addAttribute("servicios", servicioRepository.findAll());
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("clientes", clienteRepository.findAll());

        // Control de qué sección mostrar al cargar la página (inicio, solicitudes, tramites, etc.)
        model.addAttribute("seccion", seccion != null ? seccion : "inicio");

        return "SistemaNotario";
    }
}