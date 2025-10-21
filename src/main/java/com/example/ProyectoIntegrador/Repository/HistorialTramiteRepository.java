package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.HistorialTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialTramiteRepository extends JpaRepository<HistorialTramite, Integer> {
}
