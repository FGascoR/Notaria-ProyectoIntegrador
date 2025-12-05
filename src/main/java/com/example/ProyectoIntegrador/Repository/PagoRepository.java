package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Pago;
import com.example.ProyectoIntegrador.Entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    // NUEVO: Verificar si existe pago para un trámite
    boolean existsByTramite(Tramite tramite);
}