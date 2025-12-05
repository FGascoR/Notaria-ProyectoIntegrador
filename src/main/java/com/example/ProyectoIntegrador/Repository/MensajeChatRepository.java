package com.example.ProyectoIntegrador.Repository;

import com.example.ProyectoIntegrador.Entity.MensajeChat;
import com.example.ProyectoIntegrador.Entity.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Integer> {
    List<MensajeChat> findByTramiteOrderByFechaEnvioAsc(Tramite tramite);
}