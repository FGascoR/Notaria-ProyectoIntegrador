package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Reunion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReunionRepository extends JpaRepository<Reunion, Integer> {
}
