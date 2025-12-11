package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Cliente;
import com.example.ProyectoIntegrador.Entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Integer> {
    
    List<Tramite> findByClienteOrderByFechaInicioDesc(Cliente cliente);

    
    List<Tramite> findByEstado(Tramite.Estado estado);
    List<Tramite> findByFechaInicio(LocalDate fecha);

}