package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Integer> {
    // Para el historial del cliente
    List<Tramite> findByClienteOrderByFechaInicioDesc(Cliente cliente);

    // Para filtros del Notario (Búsqueda básica)
    // Puedes ampliar esto con Specifications si necesitas filtros más complejos combinados
    List<Tramite> findByEstado(Tramite.Estado estado);
    List<Tramite> findByFechaInicio(LocalDate fecha);

}