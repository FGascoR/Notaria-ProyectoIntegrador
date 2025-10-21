package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
}
