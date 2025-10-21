package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.DetallePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePagoRepository extends JpaRepository<DetallePago, Integer> {
}
