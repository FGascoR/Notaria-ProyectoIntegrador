package com.example.ProyectoIntegrador.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensaje_chat")
@Getter
@Setter
public class MensajeChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMensaje;

    @ManyToOne
    @JoinColumn(name = "idTramite", nullable = false)
    private Tramite tramite;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario; // Remitente

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private String archivoUrl;

    private LocalDateTime fechaEnvio;

    @PrePersist
    public void prePersist() {
        fechaEnvio = LocalDateTime.now();
    }
}