package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.*;
import com.example.ProyectoIntegrador.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/proceso")
public class ProcesoController {

    @Autowired private TramiteRepository tramiteRepository;
    @Autowired private MensajeChatRepository mensajeChatRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ReunionRepository reunionRepository;
    @Autowired private NotarioRepository notarioRepository;

    // Directorio donde se guardarán los archivos del chat
    private static final String UPLOAD_DIR = "uploads/chat/";

    // RUTA 1: Ver el panel del Cliente
    @GetMapping("/cliente/{idTramite}")
    public String verProcesoCliente(@PathVariable Integer idTramite, Model model, Authentication auth) {
        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);

        // Seguridad básica: si no existe el trámite, volver al inicio
        if (tramite == null) return "redirect:/Pagina";

        cargarDatosProceso(model, tramite, auth);
        return "SistemaCliente"; // Nombre del archivo HTML del cliente
    }

    // RUTA 2: Ver el panel del Notario
    @GetMapping("/notario/{idTramite}")
    public String verProcesoNotario(@PathVariable Integer idTramite, Model model, Authentication auth) {
        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);

        if (tramite == null) return "redirect:/SistemaNotario";

        cargarDatosProceso(model, tramite, auth);
        return "ProcesoNotario"; // Nombre del archivo HTML del notario
    }

    // Método auxiliar para cargar mensajes y usuario actual
    private void cargarDatosProceso(Model model, Tramite tramite, Authentication auth) {
        String username = auth.getName();
        Usuario usuarioActual = usuarioRepository.findByNombreUsuario(username).orElse(null);

        model.addAttribute("tramite", tramite);
        model.addAttribute("mensajes", mensajeChatRepository.findByTramiteOrderByFechaEnvioAsc(tramite));
        model.addAttribute("usuarioActual", usuarioActual);
    }

    // RUTA 3: Enviar mensaje (Texto y/o Archivo)
    @PostMapping("/mensaje")
    public String enviarMensaje(@RequestParam("idTramite") Integer idTramite,
                                @RequestParam(value = "contenido", required = false) String contenido,
                                @RequestParam(value = "archivo", required = false) MultipartFile archivo,
                                Authentication auth) throws IOException {

        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);
        Usuario usuario = usuarioRepository.findByNombreUsuario(auth.getName()).orElse(null);

        if (tramite != null && usuario != null) {
            MensajeChat mensaje = new MensajeChat();
            mensaje.setTramite(tramite);
            mensaje.setUsuario(usuario);
            mensaje.setContenido(contenido);

            // Lógica de subida de archivo
            if (archivo != null && !archivo.isEmpty()) {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String originalName = archivo.getOriginalFilename();
                // Generar nombre único para evitar duplicados
                String fileName = UUID.randomUUID().toString() + "_" + originalName;
                Files.write(Paths.get(UPLOAD_DIR + fileName), archivo.getBytes());

                mensaje.setArchivoUrl(fileName);
            }

            mensajeChatRepository.save(mensaje);
        }

        // Redireccionar a la vista correspondiente según el rol
        if (usuario.getRol() == Usuario.Rol.notario) {
            return "redirect:/proceso/notario/" + idTramite;
        } else {
            return "redirect:/proceso/cliente/" + idTramite;
        }
    }

    // RUTA 4: Agendar Reunión (Solo Notario)
    @PostMapping("/agendarReunion")
    public String agendarReunion(@RequestParam("idTramite") Integer idTramite,
                                 @RequestParam("fecha") LocalDateTime fecha,
                                 @RequestParam("tema") String tema,
                                 Authentication auth) {

        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);
        Usuario usuarioNotario = usuarioRepository.findByNombreUsuario(auth.getName()).orElse(null);

        if (tramite != null && usuarioNotario != null) {
            Reunion reunion = new Reunion();
            reunion.setFecha(fecha);
            reunion.setTema(tema);
            reunion.setCliente(tramite.getCliente());

            // Buscamos el objeto Notario asociado al usuario logueado
            Notario notario = notarioRepository.findByUsuario(usuarioNotario).orElse(null);
            reunion.setNotario(notario);

            reunionRepository.save(reunion);

            // Crear mensaje automático en el chat avisando de la reunión
            MensajeChat aviso = new MensajeChat();
            aviso.setTramite(tramite);
            aviso.setUsuario(usuarioNotario);
            aviso.setContenido("📅 **NUEVA REUNIÓN AGENDADA**\nTema: " + tema + "\nFecha: " + fecha.toString().replace("T", " "));
            mensajeChatRepository.save(aviso);
        }

        return "redirect:/proceso/notario/" + idTramite;
    }
}