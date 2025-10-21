package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
}
