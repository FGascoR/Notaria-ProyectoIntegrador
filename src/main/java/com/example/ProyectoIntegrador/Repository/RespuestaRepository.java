package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
}
