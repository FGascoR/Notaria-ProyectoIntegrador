package com.example.ProyectoIntegrador.Security;

import com.example.ProyectoIntegrador.Entity.Usuario;
import com.example.ProyectoIntegrador.Repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getNombreUsuario().equalsIgnoreCase(nombreUsuario))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));
        
        String roleName = "ROLE_" + usuario.getRol().name().toUpperCase();
        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getContrasena())
                //.roles(usuario.getRol().name())     // <--  REEMPLAZA ESTA LÍNEA
                .authorities(roleName)              // <--  CON ESTA
                .build();
    }
}
