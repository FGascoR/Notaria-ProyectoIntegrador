package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Integer> {
    // Buscar trámites por estado (pendiente, aceptado, etc.)
    List<Tramite> findByEstado(Tramite.Estado estado);

    // Buscar historial de trámites de un cliente específico
    List<Tramite> findByClienteOrderByFechaInicioDesc(Cliente cliente);
}