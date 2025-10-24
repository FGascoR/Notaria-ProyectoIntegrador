package com.example.ProyectoIntegrador.Controller;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.ClienteRepository;
import com.example.ProyectoIntegrador.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroController(UsuarioRepository usuarioRepository,
                              ClienteRepository clienteRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro() {
        return "Registrarse";
    }

    @PostMapping("/registrarse")
    @Transactional
    public String procesarRegistro(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("dni") String dni) {

        if (usuarioRepository.findByNombreUsuario(username).isPresent()) {
            return "redirect:/registrarse?error=true";
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(username);
        nuevoUsuario.setContrasena(passwordEncoder.encode(password));
        nuevoUsuario.setRol(Usuario.Rol.cliente);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setApellido(apellido);
        nuevoCliente.setDni(dni);
        nuevoCliente.setUsuario(usuarioGuardado);

        clienteRepository.save(nuevoCliente);

        return "redirect:/login?registro=exitoso";
    }

}