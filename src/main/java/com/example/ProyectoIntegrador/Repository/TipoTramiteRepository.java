package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.TipoTramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoTramiteRepository extends JpaRepository<TipoTramite, Integer> {
}
