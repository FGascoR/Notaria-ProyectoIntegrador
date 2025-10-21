package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Notario;
import com.example.ProyectoIntegrador.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotarioRepository extends JpaRepository<Notario, Integer> {
    Optional<Notario> findByUsuario(Usuario usuario);
}
