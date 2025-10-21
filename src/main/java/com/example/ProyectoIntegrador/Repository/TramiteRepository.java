package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramiteRepository extends JpaRepository<Tramite, Integer> {
}
