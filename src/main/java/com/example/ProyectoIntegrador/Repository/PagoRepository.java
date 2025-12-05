package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
