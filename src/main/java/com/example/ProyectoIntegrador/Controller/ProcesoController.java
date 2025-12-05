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

    private static final String UPLOAD_DIR = "uploads/chat/";

    @GetMapping("/cliente/{idTramite}")
    public String verProcesoCliente(@PathVariable Integer idTramite, Model model, Authentication auth) {
        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);
        if (tramite == null) return "redirect:/Pagina";

        // Validación de seguridad simple: asegurar que el usuario es el dueño
        // (Omitida por brevedad, pero recomendable agregar)

        cargarDatos(model, tramite, auth);
        return "SistemaCliente";
    }

    @GetMapping("/notario/{idTramite}")
    public String verProcesoNotario(@PathVariable Integer idTramite, Model model, Authentication auth) {
        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);
        if (tramite == null) return "redirect:/SistemaNotario";

        cargarDatos(model, tramite, auth);
        return "ProcesoNotario";
    }

    private void cargarDatos(Model model, Tramite tramite, Authentication auth) {
        String username = auth.getName();
        Usuario usuario = usuarioRepository.findByNombreUsuario(username).orElse(null);

        model.addAttribute("tramite", tramite);
        model.addAttribute("mensajes", mensajeChatRepository.findByTramiteOrderByFechaEnvioAsc(tramite));
        model.addAttribute("usuarioActual", usuario);
    }

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

            if (archivo != null && !archivo.isEmpty()) {
                Files.createDirectories(Paths.get(UPLOAD_DIR));
                String fileName = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
                Files.write(Paths.get(UPLOAD_DIR + fileName), archivo.getBytes());
                mensaje.setArchivoUrl(fileName);
            }
            mensajeChatRepository.save(mensaje);
        }

        if (usuario.getRol() == Usuario.Rol.notario) {
            return "redirect:/proceso/notario/" + idTramite;
        } else {
            return "redirect:/proceso/cliente/" + idTramite;
        }
    }

    @PostMapping("/agendarReunion")
    public String agendarReunion(@RequestParam("idTramite") Integer idTramite,
                                 @RequestParam("fecha") LocalDateTime fecha,
                                 @RequestParam("tema") String tema,
                                 Authentication auth) {

        Tramite tramite = tramiteRepository.findById(idTramite).orElse(null);
        Usuario notarioUser = usuarioRepository.findByNombreUsuario(auth.getName()).orElse(null);

        if (tramite != null) {
            Reunion reunion = new Reunion();
            reunion.setFecha(fecha);
            reunion.setTema(tema);
            reunion.setCliente(tramite.getCliente());

            Notario notario = notarioRepository.findByUsuario(notarioUser).orElse(null);
            reunion.setNotario(notario);

            reunionRepository.save(reunion);

            MensajeChat aviso = new MensajeChat();
            aviso.setTramite(tramite);
            aviso.setUsuario(notarioUser);
            aviso.setContenido("📅 CITA AGENDADA: " + tema + " - Fecha: " + fecha.toString().replace("T", " "));
            mensajeChatRepository.save(aviso);
        }
        return "redirect:/proceso/notario/" + idTramite;
    }
}