package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Pago;
import com.example.ProyectoIntegrador.Entity.Tramite; // <-- Importante
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // Metodo para verificar si existe un pago asociado a un trámite específico
    boolean existsByTramite(Tramite tramite);
}